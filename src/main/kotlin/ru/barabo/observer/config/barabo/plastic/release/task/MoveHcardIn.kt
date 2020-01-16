package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.template.file.FileMover
import java.time.Duration
import java.time.LocalTime

object MoveHcardIn: FileMover {

    override val pathsTo: Array<() -> String> = arrayOf(LoadRestAccount::hCardInToday)

    override val isMove: Boolean = true

    override fun name(): String = "Убрать Файлы"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom =  LocalTime.of(0, 45),
            workTimeTo = LocalTime.of(23, 45), executeWait = Duration.ZERO )

    override fun findAbstract(): Executor? = null
}