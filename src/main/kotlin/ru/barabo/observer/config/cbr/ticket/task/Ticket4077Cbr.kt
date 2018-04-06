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

object Ticket4077Cbr : FileFinder, FileProcessor {

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "4077-У Росфин"

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData("C:/PTK_POST/ELO/OUT","rf...005\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData =
            AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    private fun ticket4077cbr(): String = "K:/ARH_LEG/4077-У/квит цб/${TicketFtsText.todayFolder()}"

    override fun processFile(file: File) {

        Archive.extractFromCab(file, ticket4077cbr(), ".*\\.xml")

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, bcc = BaraboSmtp.OPER, subject = "4077-У Ответ ЦБ",
                body = "Квитки в папке ${ticket4077cbr()}")
    }
}