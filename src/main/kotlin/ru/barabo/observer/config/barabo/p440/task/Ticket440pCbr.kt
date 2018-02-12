package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.cbr.ticket.task.p440.TicketLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.IzvFromFns
import java.time.Duration
import java.time.LocalTime

object Ticket440pCbr : TicketLoader<IzvFromFns>(), FileFinder {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(Get440pFiles::getFolder440p,
            "IZVTUB_AFN_0507717_MIFNS00_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\.xml"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    override fun name(): String = "440-П Квиток на архив"

    override fun config(): ConfigTask = P440Config
}