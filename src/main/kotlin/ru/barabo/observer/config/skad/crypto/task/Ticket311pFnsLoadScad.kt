package ru.barabo.observer.config.skad.crypto.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import org.w3c.dom.Element
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Ticket311pCbr.ticket311p
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.time.LocalTime
import javax.xml.parsers.DocumentBuilderFactory

object Ticket311pFnsLoadScad :  FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(Ticket311pFnsLoadScad::class.simpleName)

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(::ticket311pDirectory, "S.*\\.xml", isModifiedTodayOnly = false)
    )

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS, isDuplicateName = false,
            workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "311-П Загрузить квитки ФНС scad"

    override fun config(): ConfigTask = ScadConfig

    private fun ticket311pDirectory() = ticket311p().byFolderExists()

    override fun processFile(file: File) {
        ScadComplex.unsignAndMoveSource(file)

        loadXmlToAfina(file)
    }

    private fun loadXmlToAfina(fileXml :File) {

        val head = DocumentBuilderFactory.newInstance()?.newDocumentBuilder()?.parse(fileXml)?.
                documentElement?.getElementsByTagName("Документ")?.item(0) as? Element

        saveTicket(head?.getAttribute("НомСооб"), head?.getAttribute("РезОбр"), fileXml)
    }

    private fun saveTicket(numberMessage: String?, resultMessage: String?, fileXml: File) {

        val number =try {
            if(numberMessage.isNullOrEmpty()) getNumberByFile(fileXml)
            else (numberMessage.trim().toLong() % 1000000).toInt()
        } catch (e: Exception) {
            logger.error("saveTicket", e)

            logger.error("numberMessage=$numberMessage")

            logger.error("fileXml=$fileXml")

            throw Exception(e.message!!)
        }

        val idRegister = AfinaQuery.selectValue(SELECT_ID_REGISTER, arrayOf(number)) as? Number ?:
        throw SessionException("Не найден ptkb_361p_register.id по номеру $number")

        val ticketBody = fileXml.readText(Charset.forName("CP1251"))

        val params :Array<Any?> = arrayOf(resultMessage, fileXml.name, ticketBody, idRegister)

        val resultCode = (AfinaQuery.execute(query = UPDATE_REGISTER, params =  params,
                outParamTypes = intArrayOf(OracleTypes.NUMBER))?.get(0) as Number).toInt()

        if(resultCode != 9) {
            sendError(resultMessage, fileXml.name, ticketBody, idRegister)
        }
    }

    private fun getNumberByFile(fileXml: File): Int = (fileXml.nameWithoutExtension
            .substringAfterLast("0000").substringBefore('_').toLong() % 1000000).toInt()


    private fun sendError(resultMessage: String?, ticketFileName: String, ticketBody: String, idRegister: Number) {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.MANAGERS_UOD, bcc = BaraboSmtp.AUTO, subject = SUBJECT_311P_ERROR,
                body = errorMessage(ticketFileName, resultMessage, ticketBody, idRegister))
    }

    private const val SELECT_ID_REGISTER = "select max(rp.id) from od.ptkb_361p_register rp where rp.NUMBER_MAIL = ?"

    private const val UPDATE_REGISTER = "{ call od.PTKB_440P.loadTicketFileFns311p(?, ?, ?, ?, ?) }"

    private const val SUBJECT_311P_ERROR = "311-П Ошибка в квитке от ФНС"

    private fun errorMessage(fileName: String, resultMessage: String?, ticketBody: String?, regId: Number) =
            "На отправленный файл получена квитанция из ФНС с ошибкой \n" +
                    "\tФайл квитка: $fileName\n" +
                    "\tКод ошибки: $resultMessage\n" +
                    "\tid : $regId\n" +
                    "\tОписание: $ticketBody"
}