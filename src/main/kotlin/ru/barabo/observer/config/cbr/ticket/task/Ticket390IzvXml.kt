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

object Ticket390IzvXml : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","ns...005\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "390-П ответ-IZV от ЦБ"

    override fun processFile(file : File) {
        val files = Archive.extractFromCab(file, Get390pArchive.getFolder390pPath())

        val listFiles = files?.joinToString("\n") { it.absolutePath } ?: Get390pArchive.getFolder390pPath()

        // TODO!!! remove after implements full functional
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = "Пришел ответ по 390-П от ЦБ IZV-файл",
                body = "Файл(ы) размещен(ы) по адресу:\n$listFiles")
    }
}