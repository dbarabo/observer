package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object OutRest: Periodical {
    override val unit: ChronoUnit = ChronoUnit.SECONDS

    override var count: Long = 60*5

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS)

    override fun name(): String = "Выгрузка Остатков"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override fun execute(elem: Elem): State {



        return State.OK
    }

}