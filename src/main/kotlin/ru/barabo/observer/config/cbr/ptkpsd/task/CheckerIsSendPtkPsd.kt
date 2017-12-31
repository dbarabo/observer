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
        if(file.exists()) {
            errorSendFileByPtkPsd(file.name)
        }
    }

   private val ERROR_SUBJECT = "Ошибка! Файл по ПТК ПСД не отправлен"

   private fun errorBody(fileName : String) = "Файл <<$fileName>> по ПТК ПСД не отправлен. Возможные причины:\n" +
           "\t1. Файл уже отправлялся (в наблюдателе в группе 'Перенос ПТК ПСД' находим и переводим в неисполнен)\n" +
           "\t2. Проблема с ПТК ПСД (закрываем - открываем снова)\n" +
           "\t3. Проблема с файлом (не проходит по схеме XML в ПТК ПСД"

   private fun errorSendFileByPtkPsd(fileName : String) {

       BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, bcc = BaraboSmtp.AUTO, subject = ERROR_SUBJECT, body = errorBody(fileName))
   }
}