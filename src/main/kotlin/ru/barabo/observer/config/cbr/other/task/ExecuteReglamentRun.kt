package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object ExecuteReglamentRun : Periodical {

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(7, 5),
            workTimeTo = LocalTime.of(11, 46) )

    override fun name(): String = "Запуск регламента"

    override fun config(): ConfigTask = AnyWork // OtherCbr

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_REGLAMENT_RUN)

        return State.OK
    }

    private const val EXEC_REGLAMENT_RUN = "{ call od.PTKB_PRECEPT.runReglamentArchiveDay }"
}