package ru.barabo.observer.config.portal.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket407XmlFromCbr: FileFinder, FileProcessor {

    override fun name(): String = "600-П Ответ UVDIFM"

    override fun config(): ConfigTask = PortalTransport

    override val fileFinderData: List<FileFinderData> =
            listOf(
                FileFinderData("Z:/foiv-fz/request", ".*\\.xml", isModifiedTodayOnly = true)
            )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))


    private fun ticket407pCb() :String = "K:/ARH_LEG/407-П/ИЭС_ЦБ/${TicketFtsText.todayFolder()}".byFolderExists().absolutePath

    override fun processFile(file: File) {

        val copyTo = File("${ticket407pCb()}/${file.name}")

        file.copyTo(copyTo)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, subject = "407-П Ответ UVDIFM",
                body = "Квиток в папке ${ticket407pCb()} файл ${file.name}")
    }
}