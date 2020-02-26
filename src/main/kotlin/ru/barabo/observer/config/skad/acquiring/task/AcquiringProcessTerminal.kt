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
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp
import java.sql.Types

object AcquiringProcessTerminal : SingleSelector {
    private val logger = LoggerFactory.getLogger(AcquiringProcessTerminal::class.java)

    override fun name(): String = "Обработка по расписанию"

    override fun config(): ConfigTask = Acquiring

    override val accessibleData = AccessibleData(WeekAccess.ALL_DAYS)

    override fun isCursorSelect(): Boolean = true

    override val select: String = "{ ? = call od.PTKB_PLASTIC_TURN.getTerminalsForProcess }"

    override fun execute(elem: Elem): State {

        val isAbsentTransact = (AfinaQuery.execute(EXEC_CHECK_TRANSACT, arrayOf(elem.idElem),
                outParamTypes = intArrayOf(Types.INTEGER))?.get(0) as? Number)?.toInt() ?: return State.OK

        if(isAbsentTransact == 0) return State.OK

        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            val posRealId = processBySchedulerId(elem.idElem!!, uniqueSession)

            AfinaQuery.commitFree(uniqueSession)

            mailInfoSuccess(posRealId)

        } catch (e: Exception) {

            logger.error("execute", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        return State.OK
    }

    private fun mailInfoSuccess(posRealId: Number) {
        val info = AfinaQuery.execute(query = CALL_INFO_POS_REAL, params = arrayOf(posRealId),
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String ?: return

        if(info.indexOf("Обработаны все док-ты:", ignoreCase = true) == 0) return

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.DELB_PLASTIC, cc = BaraboSmtp.AUTO, bcc = BaraboSmtp.DOPIKI,
                subject = SUBJECT_NONE_EXEC, body = info, charsetSubject = "UTF-8")
    }

    private fun processBySchedulerId(schedulerId: Long, uniqueSession: SessionSetting): Number {
        val row = AfinaQuery.select(SELECT_TERMINAL_INFO, arrayOf(schedulerId), uniqueSession)[0]

        val terminalId = row[0] as String

        val isUpdateVisaCourse = AfinaQuery.execute(EXEC_CLEAR_CHECK,
                arrayOf(terminalId), uniqueSession, intArrayOf(OracleTypes.NUMBER) )?.get(0) as? Number

        updateCrossCourseVisa(isUpdateVisaCourse?.toInt()?:0, terminalId, uniqueSession)

        return AfinaQuery.execute(EXEC_PROCESS_AQUIRING_TERMINAL,
                arrayOf(schedulerId), uniqueSession, intArrayOf(OracleTypes.NUMBER))?.get(0) as? Number ?:
                throw Exception("posExecReal is not found schedulerId=$schedulerId")
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

    private const val SUBJECT_NONE_EXEC =
            "✖✖✖☹☹☹✚✚✚☝☝☝✠✠✠♕♕♕ Пластик: Не все док-ты обработаны по терминалу (эквайринг)"

    private const val CALL_INFO_POS_REAL = "{ call od.PTKB_PLASTIC_TURN.infoProcessByTerminalScheduler(?, ?) }"

    private const val EXEC_CHECK_TRANSACT = "{ call od.PTKB_PLASTIC_TURN.checkSchedulerTransactExists(?, ?) }"

    private const val EXEC_UPDATE_CROSS_TRANSACT_ACQ = "{ call od.PTKB_PLASTIC_TURN.updateCrossBorderVisaAcq( ?, ? ) }"

    private const val EXEC_CLEAR_CHECK = "{ call od.PTKB_PLASTIC_TURN.clearDoubleAndCrossBorderAcq(?, ?) }"

    private const val SELECT_TERMINAL_INFO =
            "select terminal_id, trunc(sysdate) + end_time/86400 from od.PTKB_POS_SCHEDULER where id = ?"

    private const val SELECT_VISA_COURSE_TERMINAL = "{ ? = call od.PTKB_PLASTIC_TURN.getCrossBorderVisaAcq( ? ) }"

    private const val EXEC_PROCESS_AQUIRING_TERMINAL = "{ call od.PTKB_PLASTIC_TURN.processAcquiringTerminal(?, ?) }"
}