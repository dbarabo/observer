package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.task.Get440pFiles
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.cbr.ticket.task.p440.TicketLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.IzvFromFns
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket440pCbr : TicketLoader<IzvFromFns>(), FileFinder {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::getFolder440p,
            "IZVTUB_AFN_0507717_MIFNS00_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\.xml"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    fun getFolder440p() : File = File(Get440pFiles.getFolder440p())

    override fun name(): String = "440-П Квиток на архив"

    override fun config(): ConfigTask = TicketPtkPsd
}