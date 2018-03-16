package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.Verba
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket407ArjXml: FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","fz..._05\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "407-П Ответ ZRFM"

    private fun ticket407pRfm() :String = "K:/ARH_LEG/407-П/ИЭС_РФМ/${TicketFtsText.todayFolder()}"

    override fun processFile(file : File) {

        val arjArchive = Archive.extractFromCab(file, ticket407pRfm(), ".*\\.arj")

        arjArchive?.forEach {
            val xmlFiles = Archive.extractFromArj(it, ticket407pRfm(), ".*\\.xml")

            xmlFiles?.forEach { Verba.unSignFile(it) }
        }

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, subject = "407-П Ответ ZRFM", body = "Квитки в папке ${ticket407pRfm()}")
    }
}