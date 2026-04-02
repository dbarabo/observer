package ru.barabo.observer.config.fns.ens.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import org.w3c.dom.Element
import ru.barabo.archive.Archive
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.skad.crypto.task.Ticket311pFnsLoadScad
import ru.barabo.observer.config.skad.crypto.task.Ticket311pFnsLoadScad.SELECT_ID_REGISTER
import ru.barabo.observer.config.skad.crypto.task.Ticket311pFnsLoadScad.UPDATE_REGISTER
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime
import javax.xml.parsers.DocumentBuilderFactory

object LoadArchiveFromFns  : FileFinder, FileProcessor {

    override fun name(): String = "311-П ФНС грузить арх"

    override fun config(): ConfigTask = EnsConfig

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::fnsFromSmevFolder))

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun processFile(file: File) {

        val folderDestination = fnsFolderGet().absolutePath

        val zipFile = File("$folderDestination/${file.name}")

        file.copyTo(zipFile, overwrite = true)
        file.delete()

        val xmlTickets = Archive.extractFromZip(zipFile, folderDestination)

        processTickets(xmlTickets)
    }

    private fun processTickets(xmlTickets: Array<File>?) {
        if(xmlTickets == null) return

        for (ticket in xmlTickets) {
            loadXmlToAfina(ticket)
        }
    }
}

fun fnsFromSmevFolder() = FNS_FROM_SMEV.byFolderExists()

private val logger = LoggerFactory.getLogger(LoadArchiveFromFns::class.java)

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
    BaraboSmtp.sendStubThrows(to = BaraboSmtp.MANAGERS_UOD, bcc = BaraboSmtp.AUTO, subject = Ticket311pFnsLoadScad.SUBJECT_311P_ERROR,
        body = Ticket311pFnsLoadScad.errorMessage(ticketFileName, resultMessage, ticketBody, idRegister)
    )
}