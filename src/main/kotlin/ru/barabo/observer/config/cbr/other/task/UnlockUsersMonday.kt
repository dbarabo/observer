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

object UnlockUsersMonday : Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override val count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(7, 0), workTimeTo = LocalTime.of(11, 59))

    override fun isAccess()  = super.isAccess() && (LocalDate.now().dayOfWeek == DayOfWeek.MONDAY)

    override fun name(): String = "Разлочить юзеров в понедельник"

    override fun config(): ConfigTask = OtherCbr

    private val CALL_UNLOCK_USERS = "{call od.unlock_from_morningworks(?)}"

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(CALL_UNLOCK_USERS, arrayOf(String::class.javaObjectType))

        return State.OK
    }
}