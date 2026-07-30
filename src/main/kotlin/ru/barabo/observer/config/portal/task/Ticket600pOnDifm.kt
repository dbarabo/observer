package ru.barabo.observer.config.portal.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.skad.crypto.task.srcFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket600pOnDifm: FileFinder, FileProcessor {

    override fun name(): String = "600-П Ответ DIFM"

    override fun config(): ConfigTask = PortalTransport

    override val fileFinderData: List<FileFinderData> =
            listOf(
                FileFinderData("Z:/foiv-fz/request", ".*\\.arj", isModifiedTodayOnly = false)
            )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    fun ticket407pRfm(): String = "K:/ARH_LEG/407-П/ИЭС_РФМ/${TicketFtsText.todayFolder()}"

    override fun processFile(file : File) {

        Archive.extractFromArj(file, ticket407pRfm(), ".*\\.xml")

        val src = File("${srcFolder().absolutePath}/${file.name}")
        file.copyTo(src)
        if(src.exists()) {
            file.delete()
        }

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, subject = "407-П Ответ ZRFM", body = "Квитки в папке ${ticket407pRfm()}")
    }
}