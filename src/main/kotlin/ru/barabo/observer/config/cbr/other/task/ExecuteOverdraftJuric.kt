package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object ExecuteOverdraftJuric : Periodical {

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(7, 59), LocalTime.of(11, 0), Duration.ZERO)

    override fun name(): String = "Свёртка овердрафт. юрики"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXECUTE_OVERDRAFT_JURIC)

        return State.OK
    }

    private const val EXECUTE_OVERDRAFT_JURIC = "{call od.PTKB_PRECEPT.createExecuteJurOverdraft}"
}