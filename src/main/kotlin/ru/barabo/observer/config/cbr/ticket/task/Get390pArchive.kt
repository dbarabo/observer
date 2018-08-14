package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles.getFolder440p
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Get390pArchive : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","ns..._05\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "390-П Получить Архив"

    internal val X390P = if (TaskMapper.isAfinaBase()) "X:/390-П" else "C:/390-П"

    internal fun getFolder390p() : File = "$X390P/${todayFolder()}/Получено".byFolderExists()

    internal fun getFolder390pPath() = getFolder390p().absolutePath

    fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    private const val ARCHIVE_HEADER = "AFT_FTS"

    override fun processFile(file : File) {
        val files = Archive.extractFromCab(file, getFolder390pPath())

        files?.filter { it.name.indexOf(ARCHIVE_HEADER) == 0 }?.forEach {
            Archive.extractFromArj(it, getFolder440p().absolutePath)
        }

        val listFiles = files?.joinToString("\n") { it.absolutePath } ?: getFolder390pPath()

        // TODO!!! remove after implements full functional
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = "Пришли архивы 390-П",
                body = "Архив(ы) размещен(ы) по адресу:\n$listFiles")
    }
}