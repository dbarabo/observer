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
import ru.barabo.observer.crypto.Verba
import java.io.File
import java.time.Duration
import java.time.LocalTime
import java.util.regex.Pattern

object Ticket311pFns: FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","2z..._05\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "(311-П ФНС)"

    override fun processFile(file : File) {

        val files = Archive.extractFromCab(file, Ticket311pCbr.ticket311p(), ".*\\.arj")

        files?.forEach { Archive.extractFromArj(it, Ticket311pCbr.ticket311p()) }

        val search = Pattern.compile("S.*\\.XML", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        File( Ticket311pCbr.ticket311p()).listFiles {
            f ->  !f.isDirectory && search.isFind(f.name, false)}
                ?.forEach { Verba.unSignFile(it) }
    }
}
