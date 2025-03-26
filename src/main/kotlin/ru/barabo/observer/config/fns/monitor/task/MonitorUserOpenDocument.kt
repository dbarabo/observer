package ru.barabo.observer.config.fns.monitor.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.monitor.MonitorConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object MonitorUserOpenDocument  : SinglePerpetual {
    override fun name(): String = "Мониторинг пользователей"

    override fun config(): ConfigTask =  MonitorConfig

    override val unit: ChronoUnit = ChronoUnit.SECONDS
    override val countTimes: Long = 5

    override val accessibleData: AccessibleData =  AccessibleData(
        WeekAccess.ALL_DAYS, false,
        LocalTime.of(5, 0), LocalTime.of(22, 0), Duration.ZERO)

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(query = EXEC_MONITORING)

        return super.execute(elem)
    }
}


private const val EXEC_MONITORING = "{ call od.PTKB_PRECEPT.monitorUsers }"