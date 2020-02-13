package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.task.Ticket407ArjXml.ticket407pRfm
import ru.barabo.observer.config.cbr.ticket.task.TicketFtsText.ticketFtsDirectory
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.skad.crypto.task.UnCryptoScad364p.unCrypto364p
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UnSignScad364p  : FileFinder, FileProcessor {

    override fun name(): String = "Scad Снять ЭЦП"

    override fun config(): ConfigTask = ScadConfig

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(::unCrypto364p,"DT10......_......_......._._0021_0000_............\\.xml", isNegative = true),
            FileFinderData(::ticketFtsDirectory,"(FC|FS|NC|NS).*\\.xml"),
            FileFinderData(::ticket407pRfmFolder, "PI_RFM.*\\.xml")
    )

    override fun processFile(file: File) {

        ScadComplex.unsignAndMoveSource(file, srcFolder())

        if(!file.exists()) throw Exception("unsign file not found ${file.absolutePath}")
    }

    private fun ticket407pRfmFolder() = File(ticket407pRfm())
}