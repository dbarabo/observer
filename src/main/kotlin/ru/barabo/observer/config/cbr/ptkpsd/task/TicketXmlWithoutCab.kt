package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.cbr.ticket.task.TicketSimple
import ru.barabo.observer.config.cbr.ticket.task.TicketXml
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration
import java.time.LocalTime

object TicketXmlWithoutCab : FileFinder, FileProcessor {

    override fun name(): String = "Ответы (XML) незапакованные"

    override fun config(): ConfigTask = TicketPtkPsd

    override val fileFinderData: List<FileFinderData> =
        listOf(FileFinderData(TicketXml::ptkPostStore, ".*\\.xml\\.\\d\\d\\d\\d\\d\\d") )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun processFile(file: File) {

        val newFile = File("${TicketSimple.xReceiptToday()}/${file.name.substringBeforeLast('.')}")

        file.copyTo(newFile, overwrite = true)
    }
}