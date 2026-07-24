package ru.barabo.observer.config.portal.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.portal.PortalTransport
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
import java.util.regex.Pattern

object Ticket550p: FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(
                FileFinderData(
                    "Z:/foiv-wz/request",
                    "UVARH550P_0021_0000_........_...\\.xml",
                    isModifiedTodayOnly = true
                )
            )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = PortalTransport

    override fun name(): String = "764-П Ответ"

    private fun in550p() :String = "X:/639-П/In/${todayFolder()}"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    private const val SUBJECT_MAIL = "Квитки по 764-П"

    override fun processFile(file : File) {

        val ticketFile = File("${in550p()}/${file.name}")

        file.copyTo(ticketFile)

        val text = readTicketInfo(ticketFile)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, bcc = BaraboSmtp.CHECKER_550P, subject = SUBJECT_MAIL, body = text)
    }

    private fun readTicketInfo(fileTicket : File) :String {

        val textFile = fileTicket.readText()

        val matcherArchive = Pattern.compile("<ARH>(.*?)</ARH>").matcher(textFile)

        val archive = if(matcherArchive.find()) matcherArchive.group(1) else ""

        val matcherResult = Pattern.compile("<REZ_ARH>(.*?)</REZ_ARH>").matcher(textFile)

        val result = if(matcherResult.find()) matcherResult.group(1) else ""

        return "Файл квитанции:${fileTicket.name}\nОтправленный нами архив:$archive\nРезультат квитанции:$result\n"
    }
}