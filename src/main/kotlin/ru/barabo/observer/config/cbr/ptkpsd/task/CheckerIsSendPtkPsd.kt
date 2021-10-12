package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object CheckerIsSendPtkPsd : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData({File("C:/PTK_POST/POST")}),
                   FileFinderData({File("X:/Отчеты")}),
                   FileFinderData({File("c:/PTK PSD/Post/out")}))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofMinutes(10))

    override fun config(): ConfigTask = PtkPsd

    override fun name(): String = "Проверка отправки ПТК ПСД"

    override fun isThrowWhenNotExists() = false

    override fun processFile(file : File) {
        if(file.exists() && (!file.name.equals("Thumbs.db", ignoreCase = true) ) ) {
            errorSendFileByPtkPsd(file.name)
        }
    }

   private const val ERROR_SUBJECT = "Ошибка! Файл по ПТК ПСД не отправлен"

   private fun errorBody(fileName : String) = "Файл <<$fileName>> по ПТК ПСД не отправлен. Причина в программе ПТК-ПСД\n" +
           "\t1. Проблема с ПТК ПСД (закрываем - открываем снова, запускаем мониторинг, останавливаем мотиторинг - запускаем мониториг и так 2 раза)\n" +
           "\t2. Проблема с файлом (не проходит по схеме XML) в ПТК ПСД - обращаемся в ОТТС - они помогут (только они - больше никто не осилит)"

   private fun errorSendFileByPtkPsd(fileName : String) {

       BaraboSmtp.sendStubThrows(to = BaraboSmtp.PTK_PSD_CHECKER, bcc = BaraboSmtp.OPER, subject = ERROR_SUBJECT, body = errorBody(fileName))
   }
}