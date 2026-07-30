package ru.barabo.observer.config.portal.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.portal.task.TicketFtsText.ticketFts
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

object TicketFtsText : FileFinder, FileProcessor {

    override fun name(): String = "(ФТС/tz/-Текст)"

    override fun config(): ConfigTask = PortalTransport

    override val fileFinderData: List<FileFinderData> =
            listOf(
                FileFinderData("Z:/foiv-tz/request", ".*\\.xml", isModifiedTodayOnly = true)
            )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    fun ticketFts(): String = "X:/VAL/FTS/Квитки/${todayFolder()}"

    fun todayFolder(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun processFile(file: File) {
        val copyXml = File("${ticketFtsDirectory().absolutePath}/${file.name}")
        file.copyTo(copyXml)
    }
}

fun ticketFtsDirectory(): File = ticketFts().byFolderExists()