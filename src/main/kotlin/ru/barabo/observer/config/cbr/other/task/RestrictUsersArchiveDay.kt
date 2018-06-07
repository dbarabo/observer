package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object RestrictUsersArchiveDay : Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(9, 0), workTimeTo = LocalTime.of(10, 59))

    override fun name(): String = "Ограничить Арх. день"

    override fun config(): ConfigTask = OtherCbr

    private const val CALL_RESTRICT_ARCHIVE_DAY = "{ call od.PTKB_PRECEPT.restrictArchiveDay }"

    override fun execute(elem: Elem): State {

        if(LocalDate.now().dayOfWeek == DayOfWeek.TUESDAY &&
                elem.executed!! < LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 29)) ) return waitToNextTime(elem, 30)

        AfinaQuery.execute(CALL_RESTRICT_ARCHIVE_DAY)

        return State.OK
    }

    private fun waitToNextTime(elem: Elem, minute: Int): State {
        elem.executed = LocalDateTime.now().withMinute(minute)

        return State.NONE
    }
}