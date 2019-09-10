package ru.barabo.observer.config.cbr.turncard

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Clob
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object TurnOutDeleted : SinglePerpetual {
    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 3L

    override val accessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(0, 20), LocalTime.of(23, 56))

    override fun name(): String = "Выгрузка Удаленных оборотов"

    override fun config(): ConfigTask = TurnCard

    override fun execute(elem: Elem): State {
        processDelTurnCard()

        return State.NONE
    }

    private fun processDelTurnCard() {

        val dataDelete = AfinaQuery.execute(query = EXEC_TURN_OUT_DELETE,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        val isExistsTurnDelete = dataDelete?.let { it.size == 2 && it[1] != null }?:false

        if(isExistsTurnDelete) {
            OutIbi.saveFile(dataDelete!![1] as String, (dataDelete[0] as Clob).clob2string())
        }
    }

    private const val EXEC_TURN_OUT_DELETE = "{ call od.PTKB_PLASTIC_TURNOUT.turnOutDelete(?, ?) }"
}
