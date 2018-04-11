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
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object GeneralExecReserve283p : Periodical {

    val logger = LoggerFactory.getLogger(GeneralExecReserve283p::class.java)!!

    override val unit = ChronoUnit.DAYS

    override var count = 1L

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(9, 0), LocalTime.of(23, 0), Duration.ZERO)

    override fun name(): String = "Резерв учтенных %%"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        if(!isExistsGroupRateDocument() ) return waitToNextTime(elem, MINUTE_WAIT_EXISTS_GROUP_DOC)

        if(isExistsNoneExecChildDocuments() ) return waitToNextTime(elem, MINUTE_WAIT_EXEC_CHILDS)

        return createReserve()
    }

    private const val MINUTE_WAIT_EXISTS_GROUP_DOC = 5L

    private const val MINUTE_WAIT_EXEC_CHILDS = 1L

    private fun isExistsGroupRateDocument(): Boolean {
        val countGroupRateDocument = AfinaQuery.selectValue(SELECT_COUNT_GROUP_RATE_DOC) as Number

        return countGroupRateDocument.toInt() >= MUST_COUNT_GROUP_RATE_DOC
    }

    private const val SELECT_COUNT_GROUP_RATE_DOC = "SELECT od.PTKB_PRECEPT.getCountGroupRateDocuments from dual"

    private const val MUST_COUNT_GROUP_RATE_DOC = 3

    private fun isExistsNoneExecChildDocuments(): Boolean {

        val existElem = AfinaQuery.selectValue(SELECT_EXISTS_NONE_DOC) as Number

        return existElem.toInt() != 0
    }

    private const val SELECT_EXISTS_NONE_DOC = "SELECT od.PTKB_PRECEPT.isExistsNoneExecChildGroupRate from dual"

    private fun waitToNextTime(elem: Elem, nextTime: Long): State {
        elem.executed = LocalDateTime.now().plusMinutes(nextTime)

        return State.NONE
    }

    private fun createReserve(): State {
        val session = AfinaQuery.uniqueSession()

        try {
            val timeDate = AfinaQuery.selectValue(SELECT_MAX_EXECUTED_DATE) as Timestamp

            AfinaQuery.execute(EXECUTE_RESERVE_JURIC, arrayOf(timeDate), session)

            AfinaQuery.execute(EXECUTE_RESERVE_PHYSIC, arrayOf(timeDate), session)
        } catch (e: Exception) {
            ExecuteGroupRateLoan.logger.error("execute", e)

            AfinaQuery.rollbackFree(session)

            throw SessionException(e.message?:"")
        }
        AfinaQuery.commitFree(session)

        return State.OK
    }

    private const val SELECT_MAX_EXECUTED_DATE = "SELECT od.PTKB_PRECEPT.getMaxDateChildGroupRateDoc from dual"

    private const val EXECUTE_RESERVE_JURIC = "{ call od.PTKB_PRECEPT.reservTotal283pJuric(?) }"

    private const val EXECUTE_RESERVE_PHYSIC = "{ call od.PTKB_PRECEPT.reservTotal283pPhysic(?) }"
}
