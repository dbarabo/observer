package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.io.IOException
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object TtsMailFromOtk : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData("D:/Arhiv_in/OTK", "\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d\\_.*"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = OtherCbr

    override fun name(): String = "Переслать ТТС от OTK"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun processFile(file : File) {

        val directoryTo = File("${file.parent}/${todayFolder()}")
        if(!directoryTo.exists()) {
            directoryTo.mkdirs()
        }

        val destinationFile = File("${directoryTo.absolutePath}/${file.name.substring(11)}")

        if(!file.renameTo(destinationFile)) throw IOException("file it`s not renamed ${file.absolutePath}")

        sendFileIfNeed(destinationFile)
    }

    private fun sendFileIfNeed(file : File) {
        when {
            isTtsFile(file.name.trim().toUpperCase()) -> BaraboSmtp.sendToTtsFromOtk(file)
            isNoNeedSendFile(file.name.trim().toUpperCase()) -> {}
            else -> BaraboSmtp.sendToTtsUnknownFile(file)
        }
    }

    private fun isTtsFile(fileName :String) :Boolean =
            Pattern.compile("([^\\s]+(\\.(?i)(TXT))$)").matcher(fileName).find() ||
            fileName.indexOf("DKKP.") == 0 ||
            fileName.indexOf("OKUD.") == 0 ||
            fileName.indexOf("SOOB_SPR") == 0 ||
            fileName.indexOf("PTKPSD.") >= 0 ||
            fileName.indexOf("DKKP.") == 0 ||
            Pattern.compile("ЭД_\\d\\d\\d\\d.*").matcher(fileName).find()

    private fun isNoNeedSendFile(fileName :String) :Boolean =
            fileName.indexOf("VESTNIK.") >= 0 || Pattern.compile("BIC\\d\\d\\d\\d\\..*").matcher(fileName).find()

}

