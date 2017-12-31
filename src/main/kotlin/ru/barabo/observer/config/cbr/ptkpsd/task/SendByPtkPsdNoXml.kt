package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object SendByPtkPsdNoXml : FileMover, FileFinder {
    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData({ File("c:/PTK PSD/Post/out") }, Pattern.compile("Ф.*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE), true ),
                   FileFinderData({ File("c:/PTK PSD/Post/Post") }, Pattern.compile("Ф.*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE), true ),
                   FileFinderData({ File("X:/Отчеты") }, Pattern.compile("Ф.*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE), true )
                   )

    override val pathsTo: Array<()->String> = arrayOf(SendByPtkPsdCopy::cPtkPostPost, SendByPtkPsdNoXml::dArchiveOutToday)

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val isMove: Boolean = true

    override fun config(): ConfigTask = PtkPsd

    override fun name(): String = "Отправка (не XML)"

    fun dArchiveOutToday() :String = "D:/ARHIV_OUT/${todayFolder()}"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())
}