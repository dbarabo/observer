package ru.barabo.observer.config.skad.anywork.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object LockUsersInHoliday : Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
        workTimeFrom = LocalTime.of(7, 55), workTimeTo = LocalTime.of(11, 59))

    override fun name(): String = "Раз/(Л)очить отпускников"

    override fun config(): ConfigTask = AnyWork

    private const val CALL_LOCK_UNLOCK = "{ call od.PTKB_PRECEPT.lockUnlockUsersInHoliday }"

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(CALL_LOCK_UNLOCK)

        return State.OK
    }
}
