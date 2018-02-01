package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Get440pFiles : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","mz..._05\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "440-П Получить файлы"

    val X440P = if (TaskMapper.isAfinaBase()) "X:/440-П" else "C:/440-П"

    fun getFolder440p() :File = "$X440P/${todayFolder()}/Получено".byFolderExists()

    fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    private const val ARCHIVE_HEADER = "AFN_MIFNS"

    override fun processFile(file : File) {
        val files = Archive.extractFromCab(file, getFolder440p().absolutePath)

        files?.filter { it.name.indexOf(ARCHIVE_HEADER) == 0 }?.forEach {
            Archive.extractFromArj(it, getFolder440p().absolutePath)
        }
    }
}