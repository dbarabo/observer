package ru.barabo.observer.config.cbr.other.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object ExecutePosPot: Periodical {

    val logger = LoggerFactory.getLogger(ExecutePosPot::class.java)!!

    override val unit = ChronoUnit.DAYS

    override var count = 1L

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(8, 10), LocalTime.of(15, 0), Duration.ZERO)

    override fun name(): String = "Сделать ПОС-ПОТ"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        if (!isExecuteAllOverdraftFl()) return waitToNextTime(elem, MINUTE_WAIT_EXEC_CHILD)

        return createPosPot()
    }

    private const val MINUTE_WAIT_EXEC_CHILD = 1L

    private fun isExecuteAllOverdraftFl(): Boolean {
        val execAllOverdraft = AfinaQuery.selectValue(SELECT_IS_EXEC_ALL_OVERDRAFT) as Number

        return execAllOverdraft.toInt() != 0
    }

    private const val SELECT_IS_EXEC_ALL_OVERDRAFT = "SELECT od.PTKB_PRECEPT.isExecAllOverdraftFl from dual"

    private fun waitToNextTime(elem: Elem, nextTime: Long): State {
        elem.executed = LocalDateTime.now().plusMinutes(nextTime)

        return State.NONE
    }

    private fun createPosPot(): State {
        val session = AfinaQuery.uniqueSession()

        try {
            AfinaQuery.execute(query =  EXECUTE_POS_POT, sessionSetting =  session)
        } catch (e: Exception) {
            logger.error("execute", e)

            AfinaQuery.rollbackFree(session)

            throw SessionException(e.message ?: "")
        }
        AfinaQuery.commitFree(session)

        return State.OK
    }

    private const val EXECUTE_POS_POT = "{ call od.PTKB_PRECEPT.createPosPot }"
}