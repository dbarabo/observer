package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object RestrictUsersArchiveDay : Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(9, 0), workTimeTo = LocalTime.of(11, 0))

    override fun name(): String = "Ограничить Арх. день"

    override fun config(): ConfigTask = OtherCbr

    private const val CALL_RESTRICT_ARCHIVE_DAY = "{call od.PTKB_PRECEPT.restrictArchiveDay }"

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(CALL_RESTRICT_ARCHIVE_DAY)

        return State.OK
    }
}