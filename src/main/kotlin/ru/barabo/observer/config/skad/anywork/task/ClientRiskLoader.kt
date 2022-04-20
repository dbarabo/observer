package ru.barabo.observer.config.skad.anywork.task

import ru.barabo.db.SessionSetting
import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.skad.forms.clientrisk.XmlClientRiskLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.clientrisk.fromcbr.MainRisks
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.XmlLoader
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ClientRiskLoader  : FileFinder, FileProcessor {

    override fun name(): String = "Загрузка рисков клиентов из ЦБ"

    override fun config(): ConfigTask = AnyWork

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS)

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData( ::xCbrToday, ".*\\.xml"))

    override fun processFile(file: File) {

        val idRequest = loadXmlData(file)

        sendResponseByRequest(idRequest)
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

    BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, cc = BaraboSmtp.MANAGERS_UOD, bcc = BaraboSmtp.CHECKER_550P, subject = SUBJECT, body = info)
}

private fun sendResponseFound(idRequest: Number, data: List<Array<Any?>>) {

    val (fileName, countRecord, isNotEqualRisk) = AfinaQuery.select(SELECT_HEADER_EMPTY_FOUND, arrayOf(idRequest))[0]

    val info = "В файле $fileName c $countRecord записями клиентов найдены клиенты в Афине. $isNotEqualRisk"

    val content = HtmlContent(info, info, HEADER_TABLE, data)

    BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, cc = BaraboSmtp.MANAGERS_UOD, bcc = BaraboSmtp.CHECKER_CBR_RISK, subject = SUBJECT,
        body = content.html(), subtypeBody = "html")
}

private fun loadXmlData(file: File): Number {

    val fileXml = XmlClientRiskLoader<MainRisks>().load(file)

    if(fileXml.uniqueIdentifier == null || fileXml.risksReportDate == null) throw Exception("В файле ${file.name} не указан уникальный идентификатор или дата")

    val sessionSetting = AfinaQuery.uniqueSession()

    val idRequest = fileXml.saveRequest(file, sessionSetting)

    for (risk in fileXml.risks) {

        AfinaQuery.execute(EXEC_SAVE_IF_EXISTS,
            arrayOf(idRequest, risk.inn, risk.riskLevel, XmlLoader.parseDate(risk.riskDate)),
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

private const val EXEC_SAVE_IF_EXISTS = "{ call od.PTKB_CEC.riskClientSaveIfExists(?, ?, ?, ?) }"

private const val INSERT_REQUEST =
    "insert into OD.PTKB_CBR_CLIENT_RISK (id, file_name, cbr_id, cbr_date, count_records) values (?, ?, ?, ?, ?)"

private val X_CBR = "H:/Gu_cb/Уровень риска из ЦБ".ifTest("C:/311-П")

private fun xCbrToday() = File("$X_CBR/${todayFolder()}")

private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())