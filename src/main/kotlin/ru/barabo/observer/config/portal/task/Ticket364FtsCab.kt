package ru.barabo.observer.config.portal.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.skad.crypto.task.srcFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Ticket364FtsCab : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(
                FileFinderData("Z:/foiv-sz/request", ".*\\.arj", isModifiedTodayOnly = false)
            )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = PortalTransport

    override fun name(): String = "364-П ФТС Архив"

    fun archive364p() :String = "X:/364-П/${todayFolder()}"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun processFile(file: File) {

        val copyArj = File("${archive364p().byFolderExists().absolutePath}/${file.name}")

        file.copyTo(copyArj)

        Archive.extractFromArj(file, archive364p())

        val src = File("${srcFolder().absolutePath}/${file.name}")
        file.copyTo(src)
        if(src.exists()) {
            file.delete()
        }
    }
}