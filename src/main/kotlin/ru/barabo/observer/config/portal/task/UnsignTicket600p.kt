package ru.barabo.observer.config.portal.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.portal.task.Ticket407XmlFromCbr.ticket407pCb
import ru.barabo.observer.config.skad.crypto.task.srcFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UnsignTicket600p   : FileFinder, FileProcessor {

    override fun name(): String = "ПТК-PSD Снять ЭЦП"

    override fun config(): ConfigTask = TicketPtkPsd

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(
        FileFinderData(::ticket407pCbFolder, "UVDIFM_.*\\.xml"),

        FileFinderData(::ticketFtsDirectory, "UV.*\\.xml")

        //FileFinderData(::ticket364pFolder, "UV.*\\.txt")
    )

    override fun processFile(file: File) {

        ScadComplex.unsignAndMoveSource(file, srcFolder())

        if(!file.exists()) throw Exception("unsign file not found ${file.absolutePath}")
    }
}

private fun ticket407pCbFolder() = File(ticket407pCb())

private fun ticket364pFolder() = File(ticket364p())