package ru.barabo.observer.config.cbr.turncard.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.cbr.turncard.TurnCard
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Clob
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object TurnOutTechOver : SinglePerpetual {
    override val unit: ChronoUnit = ChronoUnit.HOURS

    override val countTimes: Long = 2L

    override val accessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(6, 0), LocalTime.of(23, 0))

    override fun name(): String = "Выгрузка оборотов по техн. оверам"

    override fun config(): ConfigTask = TurnCard

    override fun execute(elem: Elem): State {
        processTechOverTurnCard()

        return super.execute(elem)
    }

    private fun processTechOverTurnCard() {

        val dataDelete = AfinaQuery.execute(query = EXEC_TURN_OUT_TECH_VER,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        val isExistsTurn = dataDelete?.let { it.size == 2 && it[1] != null }?:false

        if(isExistsTurn) {
            OutIbi.saveFile(dataDelete!![1] as String, (dataDelete[0] as Clob).clob2string())
        }
    }

    private const val EXEC_TURN_OUT_TECH_VER = "{ call od.PTKB_PLASTIC_TURNOUT.turnOutTechOver(?, ?) }"
}
