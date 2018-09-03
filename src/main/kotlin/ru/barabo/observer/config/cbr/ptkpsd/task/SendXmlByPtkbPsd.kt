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
import java.time.LocalTime
import java.util.regex.Pattern

object SendXmlByPtkbPsd : FileMover, FileFinder {
    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData({ File("c:/PTK PSD/Post/out") }, Pattern.compile("(Ф|F).*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE), false ),
                    FileFinderData({ File("c:/PTK PSD/Post/Post") }, Pattern.compile("(Ф|F).*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE), false ),
                    FileFinderData({ File("X:/Отчеты") }, Pattern.compile("(Ф|F).*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE), false )
            )

    override val pathsTo: Array<()->String> = arrayOf(SendByPtkPsdCopy::cPtkPostPost, SendByPtkPsdNoXml::dArchiveOutToday)

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val isMove: Boolean = true

    override fun config(): ConfigTask = PtkPsd

    override fun name(): String = "Отправка (XML)"
}