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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TicketFsfm349p : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","oz...005\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "407-П ответ на 349-П ЦБ"

    private fun ticket349p() :String = "K:/ARH_LEG/407-П/ИЭС_ЦБ/${monthFolder()}"

    private fun monthFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM").format(LocalDate.now())

    override fun processFile(file: File) {
        val files = Archive.extractFromCab(file, ticket349p())

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, subject = name(), body = body(files) )
    }

    private fun body(files: Array<File>?): String =
            "Пришли квитки 407-П ответ на 349-П ЦБ. Файл(ы) размещены в папке ${ticket349p()}\n" +
                    "Файл(ы): ${files?.joinToString("\n ") { it.name }}"
}