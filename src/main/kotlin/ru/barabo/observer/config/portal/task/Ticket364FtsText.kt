package ru.barabo.observer.config.portal.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.mail.smtp.BaraboSmtp.CURRENCY_CONTROL
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Ticket364FtsText : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData("Z:/foiv-sz/request", "UVK.*\\.txt", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = PortalTransport

    override fun name(): String = "364-П ФТС Текст-UVKESDT"

    override fun processFile(file : File) {

        val copyTicket = File("${ticket364p()}/${file.name}")

        file.copyTo(copyTicket)

        sendInfoToMail(copyTicket)
    }

    private fun sendInfoToMail(fileTicket: File) {

        val text = fileTicket.readText(Charset.forName("CP866"))

        val subject = "Квитанция по 1459-П"

        BaraboSmtp.sendStubThrows(to=CURRENCY_CONTROL, subject=subject, body = text, attachments = arrayOf(fileTicket))
    }
}

private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

fun ticket364p() :String = "X:/364-П/Квитки/${todayFolder()}"