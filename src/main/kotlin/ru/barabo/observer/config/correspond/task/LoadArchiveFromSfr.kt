package ru.barabo.observer.config.correspond.task

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import com.thoughtworks.xstream.security.AnyTypePermission
import oracle.jdbc.OracleTypes
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.fns.ens.task.SFR_FROM_SMEV
import ru.barabo.observer.config.fns.ens.task.sfrFolderGet
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p311.ticket.sfr.DocumentTicketSfr
import ru.barabo.observer.config.task.p311.ticket.sfr.ErrorTicket
import ru.barabo.observer.config.task.p311.ticket.sfr.MainFileTicketSfr
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime

object LoadArchiveFromSfr : FileFinder, FileProcessor {


    override fun name(): String = "311-П СФР Забрать архив"

    override fun config(): ConfigTask = EnsConfig

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::sfrFromSmevFolder))

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun processFile(file: File) {

        val folderDestination = sfrFolderGet().absolutePath

        val zipFile = File("$folderDestination/${file.name}")

        file.copyTo(zipFile, overwrite = true)
        file.delete()

        val xmlTickets = Archive.extractFromZip(zipFile, folderDestination)

        processTickets(xmlTickets)
    }

    private fun processTickets(xmlTickets: Array<File>?) {
        if(xmlTickets == null) return

        for (ticket in xmlTickets) {
            processTicket(ticket)
        }
    }

    private fun processTicket(ticket: File) {

        val ticketSfr = loadTicket(ticket)

        ticketSfr.process(ticket)
    }
}

fun sfrFromSmevFolder() = SFR_FROM_SMEV.byFolderExists()

private fun loadTicket(file: File) = initXStream().fromXML(file) as MainFileTicketSfr

private fun initXStream() = XStream(DomDriver("UTF8")).apply {

    addPermission(AnyTypePermission.ANY)

    processAnnotations(MainFileTicketSfr::class.java)
    processAnnotations(DocumentTicketSfr::class.java)
    processAnnotations(ErrorTicket::class.java)

    useAttributeFor(String::class.java)
    useAttributeFor(Int::class.java)
}


private fun MainFileTicketSfr.process(file: File) {

    val ticketBody = file.readText(Charset.forName("UTF8"))

    val resultCode = (AfinaQuery.execute(EXEC_TICKET_LOAD, params = arrayOf(
        file.name,
        documentTicketSfr.codeProcessed,
        documentTicketSfr.resultProcessed,
        documentTicketSfr.dateProcessed.dateParseRu(),
        documentTicketSfr.dateMessage.dateParseRu(),
        documentTicketSfr.numberMessage), outParamTypes = intArrayOf(OracleTypes.NUMBER))?.get(0) as Number).toInt()

    if(resultCode != 9) {
        sendError(documentTicketSfr.resultProcessed, file.name, ticketBody)
    }
}

private fun sendError(resultMessage: String?, ticketFileName: String, ticketBody: String) {
    BaraboSmtp.sendStubThrows(to = BaraboSmtp.MANAGERS_UOD, bcc = BaraboSmtp.AUTO, subject = SUBJECT_311P_ERROR,
        body = errorMessage(ticketFileName, resultMessage, ticketBody))
}

private const val EXEC_TICKET_LOAD = "{ call od.PTKB_440P.ticketSfrLoad(?, ?, ?, ?, ?, ?, ?) }"

private val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy")

fun String.dateParseRu(): java.sql.Date = java.sql.Date(DATE_FORMAT.parse(this).time)

private const val SUBJECT_311P_ERROR = "311-П Ошибка в квитке от СФР"

private fun errorMessage(fileName: String, resultMessage: String?, ticketBody: String?,) =
    "На отправленный файл получена квитанция из ФНС с ошибкой \n" +
            "\tФайл квитка: $fileName\n" +
            "\tКод ошибки: $resultMessage\n" +
            "\tОписание: $ticketBody"



