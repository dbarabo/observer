package ru.barabo.observer.config.cbr.correspondent.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.correspondent.Correspondent
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UploadFromCorrespond: FileMover, FileFinder {
    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData({File("X:/Vep/out")}, isModifiedTodayOnly=true))

    override val pathsTo: Array<()->String> = arrayOf({"c:/TA/Block_ras/out/"})

    override val isMove: Boolean = false

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, true,
            LocalTime.of(0, 9), LocalTime.of(23, 51), Duration.ofSeconds(1))

    override fun name(): String = "Отправка"

    override fun config(): ConfigTask = Correspondent
}