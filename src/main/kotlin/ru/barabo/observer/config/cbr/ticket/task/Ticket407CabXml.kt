package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket407CabXml: FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","fz...005\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "407-П Ответ UVDIFM"

    private fun ticket407pCb() :String = "K:/ARH_LEG/407-П/ИЭС_ЦБ/${TicketFtsText.todayFolder()}"

    override fun processFile(file: File) {

        val files = Archive.extractFromCab(file, ticket407pCb(), ".*\\.xml")

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, subject = "407-П Ответ UVDIFM",
                body = "Квитки в папке ${ticket407pCb()} файлы ${files?.joinToString("\n ") { it.name }}")
    }
}