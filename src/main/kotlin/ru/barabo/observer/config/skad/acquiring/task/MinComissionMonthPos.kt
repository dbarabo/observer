package ru.barabo.observer.config.skad.acquiring.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.acquiring.Acquiring
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object MinComissionMonthPos : Periodical {

    override fun name(): String = "Недобранные комиссии по терминалам"

    override fun config(): ConfigTask = Acquiring

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(3, 30), workTimeTo = LocalTime.of(10, 30) )

    override fun isAccess()  = super.isAccess() && (LocalDate.now().dayOfMonth == 1)

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_CREATE_COMISSION)

        AfinaQuery.execute(EXEC_EXECUTE_COMISSION)

        return State.OK
    }

    private const val EXEC_CREATE_COMISSION = "{ call od.PTKB_PLASTIC_TURN.processMinComissionMonthPos }"

    private const val EXEC_EXECUTE_COMISSION = "{ call od.PTKB_PLASTIC_TURN.executeMinComissionMonthPos }"
}