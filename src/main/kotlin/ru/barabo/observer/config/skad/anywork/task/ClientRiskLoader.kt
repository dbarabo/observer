package ru.barabo.observer.config.skad.anywork.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.forms.clientrisk.XmlClientRiskLoader
import ru.barabo.observer.config.skad.longtime.LongTime
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.clientrisk.fromcbr.MainRisks
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.XmlLoader
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object ClientRiskLoader : FileFinder, FileProcessor {

    override fun name(): String = "Загрузка рисков клиентов из ЦБ"

    override fun config(): ConfigTask = LongTime

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(0, 0), workTimeTo = LocalTime.of(20, 0), executeWait = Duration.ofMinutes(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData( ::xCbrToday, ".*\\.xml"))

    override fun processFile(file: File) {

        val idRequest = loadXmlData(file)

        sendResponseByRequest(idRequest)

        callExtProcedureNoneException(idRequest)

    }
}

private fun callExtProcedureNoneException(idRequest: Number) {

    try {

        AfinaQuery.execute(EXEC_EXT_PROC, arrayOf(idRequest))

    } catch (e: Exception) {
        logger.error(EXEC_EXT_PROC, e)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = "FAIL FOR $EXEC_EXT_PROC", body = e.message?:"")
    }
}


private fun sendResponseByRequest(idRequest: Number) {

    val data = AfinaQuery.selectCursor(CURSOR_REPORT_CBR_RISK, arrayOf(idRequest))

    if(data.isEmpty()) {
        sendResponseNotFound(idRequest)
    } else {
        sendResponseFound(idRequest, data)
    }
}

private fun sendResponseNotFound(idRequest: Number) {

    val (fileName, countRecord, _) = AfinaQuery.select(SELECT_HEADER_EMPTY_FOUND, arrayOf(idRequest))[0]

    val info = "В файле $fileName c $countRecord записями клиентов не найдено ни одного клиента в Афине"

    BaraboSmtp.sendStubThrows(to = toEmailsList(), bcc = BaraboSmtp.CHECKER_CBR_RISK, subject = SUBJECT, body = info)
}

private fun sendResponseFound(idRequest: Number, data: List<Array<Any?>>) {

    val (fileName, countRecord, isNotEqualRisk) = AfinaQuery.select(SELECT_HEADER_EMPTY_FOUND, arrayOf(idRequest))[0]

    val info = "В файле $fileName c $countRecord записями клиентов найдены клиенты в Афине. $isNotEqualRisk"

    val content = HtmlContent(info, info, HEADER_TABLE, data)

    BaraboSmtp.sendStubThrows(to = toEmailsList(), bcc = BaraboSmtp.CHECKER_CBR_RISK, subject = SUBJECT,
        body = content.html(), subtypeBody = "html")
}

fun toEmailsList():Array<String> {

    val toEmails = AfinaQuery.selectValue(SELECT_TO_EMAILS) as String

    return toEmails.split(';').toTypedArray()
}

private val logger = LoggerFactory.getLogger(ClientRiskLoader::class.java)

private fun loadXmlData(file: File): Number {

    val fileXml = XmlClientRiskLoader<MainRisks>().load(file)

    if(fileXml.uniqueIdentifier == null || fileXml.risksReportDate == null) throw Exception("В файле ${file.name} не указан уникальный идентификатор или дата")

    AfinaQuery.execute(EXEC_TRUNC_TABLE)

    val sessionSetting = AfinaQuery.uniqueSession()

    val idRequest = fileXml.saveRequest(file, sessionSetting)

    for (risk in fileXml.risks) {

        if(risk.inn == null || risk.riskLevel == null) continue

        AfinaQuery.execute(EXEC_SAVE_IF_EXISTS,
            arrayOf(idRequest, risk.inn, risk.riskLevel, XmlLoader.parseDate(risk.riskDate),
                risk.mainRisk, risk.addRisk1, risk.addRisk2, risk.addRisk3),
            sessionSetting)
    }

    AfinaQuery.commitFree(sessionSetting)

    return idRequest
}

private fun MainRisks.saveRequest(file: File, sessionSetting: SessionSetting): Number {
    val sequence = AfinaQuery.nextSequence(sessionSetting)

    val params: Array<Any?> = arrayOf(sequence, file.name, this.uniqueIdentifier,
        this.risksReportDate, (this.risks?.size ?: 0) )

    AfinaQuery.execute(INSERT_REQUEST, params, sessionSetting)

    return sequence
}

private val HEADER_TABLE = mapOf(
    "Клиент" to "left",
    "ИНН" to "left",
    "Счет клиента" to "left",
    "Уровень Риска ЦБ"  to "right",
    "Дата Риска ЦБ"  to "right",
    "Уровень Риска Афина"  to "right")

private const val SUBJECT = "Проверка файла Уровень риска клиентов от ЦБР"

private const val SELECT_HEADER_EMPTY_FOUND =
    "select file_name, count_records, od.XLS_REPORT_ALL.isEqualsRiskCbr(id) from OD.PTKB_CBR_CLIENT_RISK where id = ?"

private const val CURSOR_REPORT_CBR_RISK = "{ ? = call od.XLS_REPORT_ALL.getRiskClientCbrByFileId( ? ) }"

private const val EXEC_EXT_PROC = "{ call od.DPC_PTKB_ACCOUNTBLOCK_ZSK(?)  }"

private const val EXEC_TRUNC_TABLE = "{ call od.PTKB_CEC.truncateCbrClientRiskAll }"

private const val EXEC_SAVE_IF_EXISTS = "{ call od.PTKB_CEC.riskClientSaveIfExists(?, ?, ?, ?, ?, ?, ?, ?) }"

private const val INSERT_REQUEST =
    "insert into OD.PTKB_CBR_CLIENT_RISK (id, file_name, cbr_id, cbr_date, count_records) values (?, ?, ?, ?, ?)"

private val X_CBR = "H:/Gu_cb/Уровень риска из ЦБ".ifTest("C:/311-П")

private fun xCbrToday() = File("$X_CBR/${todayFolder()}")

fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

private const val SELECT_TO_EMAILS = "select OD.GetEmailByPTKBNotification('PTKB_CheckRiskCbr') from dual"