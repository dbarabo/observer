package ru.barabo.observer.config.cbr.ticket.task

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p311.ticket.FileRecord
import ru.barabo.observer.config.task.p311.ticket.NameRecords
import ru.barabo.observer.config.task.p311.ticket.TicketCbr
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration
import java.time.LocalTime

object XmlLoaderCbrTicket311p : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(XmlLoaderCbrTicket311p::folderTicket311p, "UV.N07717.*\\.xml"))

    override val accessibleData: AccessibleData =
            AccessibleData(WeekAccess.WORK_ONLY, false, LocalTime.of(9, 0), LocalTime.of(23, 0), Duration.ZERO)

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "311-П Квитки ЦБ"

    override fun processFile(file: File) {

        val ticket = loadTicket(file)

        ticket.updateArchiveResultCode()

        ticket.updateFilesTicket()
    }

    private fun TicketCbr.updateFilesTicket() {

        nameRecords?.fileRecords?.forEach {
            AfinaQuery.execute(EXEC_FILE_TICKET_LOAD, arrayOf(it.fileName.trim().substringBeforeLast("."), resultArchive.trim()))
        }
    }


    private fun TicketCbr.updateArchiveResultCode() {
        val archiveName = archiveName?.trim()?.substringBeforeLast(".") ?: throw SessionException("Не найдено имя архива")

        AfinaQuery.execute(EXEC_ARCHIVE_TICKET_LOAD, arrayOf(archiveName, resultArchive.trim()))
    }

    private const val EXEC_FILE_TICKET_LOAD = "{ call od.PTKB_440P.loadTicketCbrFile311p(?, ?) }"

    private const val EXEC_ARCHIVE_TICKET_LOAD = "{ call od.PTKB_440P.loadTicketArchive311p(?, ?) }"

    private fun loadTicket(file: File) = initXStream().fromXML(file) as TicketCbr

    private fun initXStream() = XStream(DomDriver("CP1251")).apply {
        processAnnotations(TicketCbr::class.java)
        processAnnotations(NameRecords::class.java)
        processAnnotations(FileRecord::class.java)

        useAttributeFor(String::class.java)
        useAttributeFor(Int::class.java)
    }

    private fun folderTicket311p() = File(Ticket311pCbr.ticket311p())
}