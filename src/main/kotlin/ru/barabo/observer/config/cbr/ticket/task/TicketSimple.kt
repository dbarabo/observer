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

object TicketSimple : FileMover, FileFinder {
    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT",
                    "(0c|0d|0f|0h|0k|0p|0q|0r|0t|0u|0w|0x|0y|1d|1e|1f|1m|1u|1w|1x|1y|2m|2p|2q|3e|3h|3m|3n|4c|4e|" +
                            "4h|4q|5b|5c|5d|6b|6e|6j|6k|6t|6z|7e|7g|7m|7n|7s|7u|7v|7w|7z|8f|8l|8r|8x|fz|k8|k9|km|kn|kq|" +
                            "ld|m4|m5|m6|m9|ma|mb|mg|n0|n1|n3|n4|n5|n6|n7|n8|n9|na|nb|ng|ns|nt|o3|oa|pk|pl|pu|r1|rf|s1" +
                            "|s2|s5|s6|s7|s8|s9|sa).0.(0|_)05\\.717", isModifiedTodayOnly = true)
            )

    override val pathsTo: Array<()->String> = arrayOf(TicketSimple::xReceiptToday)

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val isMove: Boolean = false

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "Разные (не XML)"

    fun xReceiptToday() :String = "X:/RECEIPT/${todayFolder()}"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())
}
