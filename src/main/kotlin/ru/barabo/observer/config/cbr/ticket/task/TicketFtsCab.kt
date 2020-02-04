package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration
import java.time.LocalTime
import java.util.regex.Pattern

object TicketFtsCab: FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","tz..._05\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "ФТС-Архивы"

    override fun processFile(file : File) {

        val files = Archive.extractFromCab(file, TicketFtsText.ticketFts(), ".*\\.arj")

        files?.forEach { Archive.extractFromArj(it, TicketFtsText.ticketFts()) }

        val search = Pattern.compile(".*\\.XML", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        File(TicketFtsText.ticketFts()).listFiles {
            f -> (!f.isDirectory) && search.isFind(f.name, false)   }?.forEach {
        }
    }
}