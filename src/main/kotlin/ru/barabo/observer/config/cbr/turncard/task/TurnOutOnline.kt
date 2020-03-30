package ru.barabo.observer.config.cbr.turncard.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.db.TransactType
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

object TurnOutOnline : SinglePerpetual {

    private val logger = LoggerFactory.getLogger(TurnOutOnline::class.java)

    override val unit: ChronoUnit = ChronoUnit.SECONDS

    override val countTimes: Long = 10L

    override val accessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(0, 20), LocalTime.of(23, 56))

    override fun name(): String = "Выгрузка оборотов Online"

    override fun config(): ConfigTask = TurnCard

    override fun execute(elem: Elem): State {
        processTurnCard()

        return super.execute(elem)
    }

    private val uniqueIdSession: Long = AfinaQuery.uniqueSession().idSession!!

    private fun processTurnCard() {

        val uniqueSessionCommit = SessionSetting(false,  TransactType.COMMIT_MONOPOLY, uniqueIdSession)

        val data = AfinaQuery.execute(query = EXEC_TURN_OUT, sessionSetting = uniqueSessionCommit,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        val isExistsTurn = data?.let { it.size == 2 && it[1] != null } ?: false

        if(isExistsTurn) {
            OutIbi.saveFile(data!![1] as String, (data[0] as Clob).clob2string())
        }
    }

    private const val EXEC_TURN_OUT = "{ call od.PTKB_PLASTIC_TURNOUT.turnOut(?, ?) }"
}