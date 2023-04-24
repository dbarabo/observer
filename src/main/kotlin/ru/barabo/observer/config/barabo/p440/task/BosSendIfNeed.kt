package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object BosSendIfNeed : Periodical {
    override fun name(): String = "Отправка Остатков по старым RPO"
    override fun config(): ConfigTask = EnsConfig // P440Config

    override val accessibleData: AccessibleData =  AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(7, 55), LocalTime.of(16, 0), Duration.ZERO)

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null
    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_CREATE_BOS)

        return State.OK
    }
}

private const val EXEC_CREATE_BOS = "{ call od.PTKB_440P.sendBosIfNeed }"