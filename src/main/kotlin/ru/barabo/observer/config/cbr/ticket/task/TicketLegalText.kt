package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TicketLegalText : FileFinder, FileMover {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","0z...005\\.717", isModifiedTodayOnly = true))

    override val pathsTo: Array<() -> String> = arrayOf(TicketLegalText::ticketLegalText)

    override val isMove: Boolean = false

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "(Легализация-Текст)"

    private fun ticketLegalText() :String = "k:/ARH_LEG/kvit_gu/${monthFolder()}"

    fun monthFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM").format(LocalDate.now())
}
