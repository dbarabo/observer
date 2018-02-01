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

object OutIbiAll : Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override val count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(7, 0), LocalTime.of(8, 0))

    override fun name(): String = "Выгрузка оборотов Проверочная"

    override fun config(): ConfigTask = PlasticTurnConfig

    private const val EXEC_TURN_OUT_ALL = "{ call od.PTKB_PLASTIC_TURNOUT.turnOutAllPeriod(?, ?) }"

    override fun execute(elem: Elem): State {

        val data = AfinaQuery.execute(query = EXEC_TURN_OUT_ALL,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        val isExistsTurn = data?.let { it.size == 2 && it[1] != null } ?: false

        if (isExistsTurn) {
            OutIbi.saveFile(data!![1] as String, (data[0] as Clob).clob2string())
        }

        return State.OK
    }
}