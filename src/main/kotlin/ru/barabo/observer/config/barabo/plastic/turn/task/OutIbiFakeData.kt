package ru.barabo.observer.config.barabo.plastic.turn.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Clob
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object OutIbiFakeData : Periodical {
    override val unit: ChronoUnit = ChronoUnit.YEARS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(14, 0), LocalTime.of(15, 0), null)

    override fun name(): String = "Выгрузка фейковых оборотов НЕ Исполнять!!!"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {
        val data = AfinaQuery.execute(query = EXEC_TURN_OUT,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        OutIbi.saveFile(data!![1] as String, (data[0] as Clob).clob2string())

        return State.OK
    }

    private const val EXEC_TURN_OUT = "{ call od.PTKB_PLASTIC_TURNOUT.testHmac(?, ?) }"
}