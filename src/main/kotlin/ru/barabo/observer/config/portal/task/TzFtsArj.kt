package ru.barabo.observer.config.portal.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.portal.task.TicketFtsText
import ru.barabo.observer.config.portal.task.TicketFtsText.ticketFtsDirectory
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.skad.crypto.task.srcFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration
import java.time.LocalTime

object TzFtsArj: FileFinder, FileProcessor {

    override fun name(): String = "ФТС(tz)-Архивы"

    override fun config(): ConfigTask = PortalTransport

    override val fileFinderData: List<FileFinderData> =
            listOf(
                FileFinderData("Z:/foiv-tz/request", ".*\\.arj", isModifiedTodayOnly = false)
            )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun processFile(file : File) {

        val copyArj = File("${ticketFtsDirectory().absolutePath}/${file.name}")

        file.copyTo(copyArj)

        Archive.extractFromArj(file, TicketFtsText.ticketFts())

        val src = File("${srcFolder().absolutePath}/${file.name}")
        file.copyTo(src)
        if(src.exists()) {
            file.delete()
        }
    }
}