package ru.barabo.observer.config.skad.acquiring.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.exchange.VisaCalculator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.acquiring.Acquiring
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp

object AcquiringProcessTerminal : SingleSelector {
    private val logger = LoggerFactory.getLogger(AcquiringProcessTerminal::class.java)

    override fun name(): String = "Обработка по терминалу и времени"

    override fun config(): ConfigTask = Acquiring

    override val accessibleData = AccessibleData(WeekAccess.ALL_DAYS)

    override fun isCursorSelect(): Boolean = true

    override val select: String = "{ ? = call od.PTKB_PLASTIC_TURN.getTerminalsForProcess }"

    override fun execute(elem: Elem): State {
        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            processBySchedulerId(elem.idElem!!, uniqueSession)

            AfinaQuery.commitFree(uniqueSession)

        } catch (e: Exception) {

            logger.error("execute", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        return State.OK
    }

    private fun processBySchedulerId(schedulerId: Long, uniqueSession: SessionSetting) {
        val row = AfinaQuery.select(SELECT_TERMINAL_INFO, arrayOf(schedulerId), uniqueSession)[0]

        val terminalId = row[0] as String

        val isUpdateVisaCourse = AfinaQuery.execute(EXEC_CLEAR_CHECK,
                arrayOf(terminalId), uniqueSession, intArrayOf(OracleTypes.NUMBER) )?.get(0) as? Number

        updateCrossCourseVisa(isUpdateVisaCourse?.toInt()?:0, terminalId, uniqueSession)

        /*val posExecReal = */AfinaQuery.execute(EXEC_PROCESS_AQUIRING_TERMINAL,
                arrayOf(schedulerId), uniqueSession, intArrayOf(OracleTypes.NUMBER))
    }

    private fun updateCrossCourseVisa(isUpdateVisaCourse: Int, terminalId: String, uniqueSession: SessionSetting) {
        if(isUpdateVisaCourse == 0) return

        val data = AfinaQuery.selectCursor(SELECT_VISA_COURSE_TERMINAL, arrayOf(terminalId), uniqueSession)

        for(row in data) {
            val id = row[0]

            val rurAmount = (row[1] as Number).toLong()

            val dateOper = (row[2] as Timestamp).toLocalDateTime()

            val usdCent = try {
                VisaCalculator.convertRurToUsd(rurAmount, dateOper)
            } catch (e: Exception) {
                Long::class.javaObjectType
            }

            AfinaQuery.execute(EXEC_UPDATE_CROSS_TRANSACT_ACQ, arrayOf(id, usdCent), uniqueSession)
        }
    }

    private const val EXEC_UPDATE_CROSS_TRANSACT_ACQ = "{ call od.PTKB_PLASTIC_TURN.updateCrossBorderVisaAcq( ?, ? ) }"

    private const val EXEC_CLEAR_CHECK = "{ call od.PTKB_PLASTIC_TURN.clearDoubleAndCrossBorderAcq(?, ?) }"

    private const val SELECT_TERMINAL_INFO =
            "select terminal_id, trunc(sysdate) + end_time/86400 from od.PTKB_POS_SCHEDULER where id = ?"

    private const val SELECT_VISA_COURSE_TERMINAL = "{ ? = call od.PTKB_PLASTIC_TURN.getCrossBorderVisaAcq( ? ) }"

    private const val EXEC_PROCESS_AQUIRING_TERMINAL = "{ call od.PTKB_PLASTIC_TURN.processAcquiringTerminal(?, ?) }"
}