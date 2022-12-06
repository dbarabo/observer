package ru.barabo.observer.config.skad.plastic.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ptkpsd.task.CheckerIsSendPtkPsd
import ru.barabo.observer.config.skad.crypto.task.AddSign600P
import ru.barabo.observer.config.skad.crypto.task.nameDateToday
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object CheckerIsSendKyccl : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
        listOf( FileFinderData(::folderRiskCbr, "KYCCL_2540015598_0021_.*\\.xml") )

    override val accessibleData: AccessibleData =
        AccessibleData(WeekAccess.WORK_ONLY, true, LocalTime.of(15, 0), LocalTime.of(21, 0),
            Duration.ofMinutes(59))

    override fun config(): ConfigTask = PtkPsd

    override fun name(): String = "Проверка отправки файла Клиентов с Рисками ЦБ"

    override fun isThrowWhenNotExists() = false

    override fun processFile(file: File) {
        if(file.isNotExistsFolderSent() ) {
            errorSendFile(file.name)
        }
    }

    private fun errorSendFile(fileName: String) {

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.KYCCL_CHECKER, bcc = BaraboSmtp.OPER, subject = ERROR_SUBJECT,
            body = errorBody(fileName)
        )
    }
}

private fun errorBody(fileName : String) = "Похоже файл <<$fileName>> автоматически не отправлен ч/з личный кабинет ЦБ\n" +
        "Нужно попросить ДТТС проверить работоспособность компьютера, на котором находится отправка в л/к ЦБ"

private const val ERROR_SUBJECT = "Ошибка! Файл Клиентов с Рисками ЦБ не отправлен"

private fun File.isNotExistsFolderSent(): Boolean = !File("$parent/Sent/$name").exists()

private fun folderRiskCbr() = File(pathFolderRiskCbr())

