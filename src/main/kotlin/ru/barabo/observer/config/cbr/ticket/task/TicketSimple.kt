package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.PrefixTicket
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import ru.barabo.observer.store.Elem
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TicketSimple : FileMover, FileFinder {
    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","...0.(0|_)05\\.717", isModifiedTodayOnly = true)
            )

    override fun isContainsTask(task: ActionTask?): Boolean = task?.config() == this.config()

    override fun createNewElem(file: File): Elem {

        val ticketTask = PrefixTicket.ticketByPrefix(file.name, this)

        return Elem(file, ticketTask, accessibleData.executeWait)
    }

    override val pathsTo: Array<()->String> = arrayOf(TicketSimple::xReceiptToday)

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val isMove: Boolean = false

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "Разные (не XML)"

    fun xReceiptToday(): String = "X:/RECEIPT/${todayFolder()}"

    private fun todayFolder(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())
}
