package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.cmd.Cmd
import ru.barabo.cmd.deleteFolder
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

object EfudkoResponseObserver : FileFinder, FileProcessor {

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "Эфюдко пришло"

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(TicketXml::ptkPostStore, ".*\\.(xls|pdf|doc|jpg|png|jpeg|docx|xlsx)\\.\\d\\d\\d\\d\\d\\d") )

    override val accessibleData: AccessibleData =
            AccessibleData(workTimeFrom = LocalTime.of(0, 10),
                    workTimeTo = LocalTime.of(23, 50), executeWait = Duration.ZERO)

    override fun processFile(file: File) {

        val fileName = file.name.substringBeforeLast('.')

        val tempFile = File("""${Cmd.createFolder("e").absoluteFile}/$fileName""")

        file.copyTo(tempFile)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, bcc = BaraboSmtp.OPER, subject = name(),
                body = "Похоже, что ${name()}", attachments = arrayOf(tempFile))

        tempFile.parentFile.deleteFolder()
    }
}