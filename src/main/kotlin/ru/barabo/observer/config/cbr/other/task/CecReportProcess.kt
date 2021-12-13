package ru.barabo.observer.config.cbr.other.task

import ru.barabo.cmd.XmlValidator
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.other.task.cec.FileXml
import ru.barabo.observer.config.cbr.other.task.cec.Person
import ru.barabo.observer.config.cbr.other.task.cec.XmlCecLoader
import ru.barabo.observer.config.cbr.other.task.cec.Zapros
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.sql.Clob
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CecReportProcess : FileFinder, FileProcessor {

    override fun name(): String = "Запрос из ЦИК"

    override fun config(): ConfigTask = AnyWork // OtherCbr

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS)

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData( ::xCecToday, ".*\\.xml"))

    private val X_CEC = "X:/ЦИК".ifTest("C:/ЦИК")

    private fun xCecToday() = File("$X_CEC/${todayFolder()}/Запрос")

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy.MM.dd").format(LocalDate.now())

    private fun xCecResponseToday() = "$X_CEC/${todayFolder()}/Ответ".byFolderExists()

    override fun processFile(file: File) {

        val idRequest = loadXmlData(file)

        idRequest?.let { request ->
            val fileResponse = sendResponseData(request, file)

            fileResponse?.let {XmlValidator.validate(it, XSD_SCHEMA_PATH) }

            sendMailData(fileResponse, file)
        }
            ?: sendMailData(null, file)
    }

    private fun loadXmlData(file: File) :Number? {

        val fileXml = XmlCecLoader<FileXml>().load(file)

        if(fileXml.persons == null || fileXml.zapros == null) return null

        val sessionSetting = AfinaQuery.uniqueSession()

        val idRequest = fileXml.zapros.saveRequest(file, sessionSetting)

        fileXml.persons?.forEach { it.save(idRequest, sessionSetting) }

        AfinaQuery.commitFree(sessionSetting)

        return idRequest
    }

    private const val XSD_SCHEMA_PATH = "/xsd/VO_CIK_CB_7.xsd"

    private const val SUBJECT_CEC = "ЦИК ОТЧЕТ (CEC REPORT)"

    private fun bodyFileCec(file: File) = "файл для отправки данных находится по адресу ${file.absolutePath}"

    private fun bodyEmptyCec(fileRequest: File) = "нет совпадений после обработки файла ${fileRequest.absolutePath}"

    private fun sendMailData(file: File?, fileRequest: File) {

        val body = file?.let { bodyFileCec(it) } ?: bodyEmptyCec(fileRequest)

        val attachment = file?.let { arrayOf(it) } ?: arrayOf(emptyResponse(fileRequest))

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.MANAGERS_UOD, bcc = BaraboSmtp.CHECKER_550P, subject = SUBJECT_CEC,
                body = body, attachments = attachment)
    }

    private const val SELECT_FILENAME = "select od.PTKB_CEC.getFileName(?) from dual"

    private const val SELECT_DEPUTY_DATA = "select od.PTKB_CEC.getDeputyData(?, ?) from dual"

    private fun sendResponseData(idRequest :Number, file: File) :File? {

        val fileResponseName = AfinaQuery.selectValue(SELECT_FILENAME, arrayOf(file.name)) as String

        val data = AfinaQuery.selectValue(SELECT_DEPUTY_DATA, arrayOf(idRequest, file.name)) as Clob? ?: return null

        val fileResponse = File("${xCecResponseToday().absolutePath}/$fileResponseName")

        fileResponse.writeText(data.clob2string())

        return fileResponse
    }

    /**
     * return empty response by file request fileRequest
     */
    private fun emptyResponse(fileRequest: File): File {

        val responseDateTime = LocalDateTime.now()

        val requestNumber = fileRequest.nameWithoutExtension.substringAfter("_Z_")
            .takeIf { it.length <  fileRequest.nameWithoutExtension.length}
            ?: fileRequest.nameWithoutExtension.substringAfter("_P_")

        val responseFile = File("${xCecResponseToday().absolutePath}/${OUR_CODE}_${responseDateTime.formatDateTime()}_K_${requestNumber}_1000_$CEC_CODE.xml")

        val textResponse = emptyTicketTemplate(responseFile.nameWithoutExtension, fileRequest.nameWithoutExtension, responseDateTime.formatDateDDMMYYYY() )

        responseFile.writeText(textResponse)

        return responseFile
    }

    private const val OUR_CODE = "K1022500001325"

    private const val CEC_CODE = "F1027700466640"

    private const val INSERT_REQUEST = "insert into OD.PTKB_IZBIRKOM_REQUEST (id, FILE_NAME, ID_REQ, DATE_REQ) " +
            "values (?, ?, ?, to_date(?, 'dd.mm.yyyy') )"

    private fun Zapros.saveRequest(file: File, sessionSetting : SessionSetting) :Number {
        val sequence = AfinaQuery.nextSequence(sessionSetting)

        val params :Array<Any?> = arrayOf(sequence, file.name, this.id, this.date)

        AfinaQuery.execute(INSERT_REQUEST, params, sessionSetting)

        return sequence
    }

    private const val INSERT_PERSON = "insert into OD.PTKB_IZBIRKOM ( ID_REQUEST, id, PERS_FAMILY, BIRTHDAY, DOC_CODE,  DOC_SERIA, " +
            " DOC_NUMBER, NEKONF_ADRESS,  BIRTHPLACE,  SERV_VRNKAND, SERV_CODE_SUBJ, SERV_COMPANY, SERV_SYST, SERV_NAME, " +
            "SERV_SUBJ,  SERV_DATE, PERS_ID, PERS_NAME, PERS_SURNAME, KONF_ADRESS, PERS_CODE_ADR) " +
            "values (?, classified.nextval, ?, to_date(?, 'dd.mm.yyyy'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            "to_date(?, 'dd.mm.yyyy'), ?, ?, ?, ?, ?)"

    private fun Person.save(idRequest :Number, sessionSetting :SessionSetting) {

        val params :Array<Any?> = arrayOf(
                idRequest,
                persInfo.fio.fam,
                persInfo.fio.getbirth().trim(),
                persInfo.doc.kodVidDoc,
                persInfo.doc.seria,
                persInfo.doc.number,
                persInfo.adress.neConfAdress,
                persInfo.adress.birthPlace,
                slugaInfo.vr,
                slugaInfo.idInfo.code,
                slugaInfo.idInfo.company,
                slugaInfo.idInfo.systema,
                slugaInfo.nameInfoSluga.vibory,
                slugaInfo.nameInfoSluga.subject,
                slugaInfo.nameInfoSluga.date,
                id,
                persInfo.fio.name,
                persInfo.fio.second,
                persInfo.adress.confAdress,
                persInfo.adress.codeSubj
        )

        AfinaQuery.execute(INSERT_PERSON, params, sessionSetting)
    }

    private fun emptyTicketTemplate(responseFileNoExt: String,
                                    fileRequestNoExt: String,
                                    createDate: String) /*dd.MM.yyyy*/ =
            """<?xml version="1.0" encoding="UTF-8"?>
<File xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="VO_CIK_CB_K_6.xsd">
  <AcknowledgementType>10</AcknowledgementType>
  <ResultCode>1000</ResultCode>
  <ResultText>Информация по проверяемым лицам отсутствует</ResultText>
  <To>$CEC_CODE</To>
  <From>$OUR_CODE</From>
  <MessageID>${responseFileNoExt}</MessageID>
  <CorrelationMessageID>${fileRequestNoExt}</CorrelationMessageID>
  <MessageType>2</MessageType>
  <Priority>5</Priority>
  <CreateTime>$createDate</CreateTime>
</File>"""

    private fun LocalDateTime.formatDateTime() = DateTimeFormatter.ofPattern("ddMMyy_HHmmss").format(this)

    private fun LocalDateTime.formatDateDDMMYYYY() = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(this)
}