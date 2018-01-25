package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount.hCardIn
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.io.File

object GetFromJzdo: FileMover, FileFinder {

    override fun name(): String = "Забрать файл из ПЦ"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true)

    val GET_FROM_JZDO = "\\\\jzdo/c$/jzdo/files/doc/in/cs/unknown"

    fun getFromJzdo(): File = File(GET_FROM_JZDO)

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::getFromJzdo))

    override val pathsTo: Array<() -> String> = arrayOf(::hCardIn)

    override val isMove: Boolean = true
}