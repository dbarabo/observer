package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.time.LocalTime

object SendToJzdo : FileMover, FileFinder {

    override fun name(): String = "Отправить файл в ПЦ"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(7, 0))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(OutIbi::hCardOutTodayFolder, "(IIA_|RATE|ZWU_).*"))

    override val pathsTo: Array<() -> String> = arrayOf(IbiSendToJzdo::hCardOutSentTodayByFolder, IbiSendToJzdo::toJzdoSent)

    override val isMove: Boolean = true
}