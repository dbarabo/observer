package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object ResponseXlsFor: FileFinder, FileProcessor {

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "XLS-файл ФОР"

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(TicketXml::ptkPostStore, "s7.*\\.717\\.\\d\\d\\d\\d\\d\\d") )

    override val accessibleData: AccessibleData =
            AccessibleData(workTimeFrom = LocalTime.of(6, 0),
                    workTimeTo = LocalTime.of(23, 0), executeWait = Duration.ZERO)

    private const val TICKET_342P_FOLDER: String = "X:/OBVED/342-П резервы"

    override fun processFile(file: File) {

        val (xls) = Archive.extractFromCab(file, TICKET_342P_FOLDER, ".*\\.xls")!!

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.BOOKER, bcc = BaraboSmtp.OPER, subject = "Файл XLS для ФОР",
                body = "Файл размещен по адресу $TICKET_342P_FOLDER/${xls.name}", attachments = arrayOf(xls) )
    }
}