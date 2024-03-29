package ru.barabo.observer.config.barabo.p440.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
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

object RooWaitCancel : Periodical {
    override fun name(): String = "Зависшие блокировки - Проверка"

    override fun config(): ConfigTask =  EnsConfig // P440Config

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData=  AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(7, 0), LocalTime.of(18, 0), Duration.ZERO)

    private const val EXEC_DESICION_CANCEL = "call od.PTKB_440P.processDesicionCancelWait(?)"

    override fun execute(elem: Elem): State {

        val error = AfinaQuery.execute(query = EXEC_DESICION_CANCEL,
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String

        if(error != null) {
            throw Exception(error)
        }

        return State.OK
    }
}