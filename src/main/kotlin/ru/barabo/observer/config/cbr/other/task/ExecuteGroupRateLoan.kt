package ru.barabo.observer.config.cbr.other.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object ExecuteGroupRateLoan: Periodical {

    val logger = LoggerFactory.getLogger(ExecuteGroupRateLoan::class.java)!!

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(10, 0), LocalTime.of(23, 50), Duration.ofHours(5))

    override fun name(): String = "Учет Всех %% Сегодня"

    override fun config(): ConfigTask = AnyWork // OtherCbr

    override fun execute(elem: Elem): State {

        if(LocalTime.now().hour >= 21) {
            elem.executed = LocalDateTime.now()
            return State.ARCHIVE
        }

        val session = AfinaQuery.uniqueSession()

        try {
            AfinaQuery.execute(query = EXECUTE_GROUP_RATE_LOAN_JURIC, sessionSetting = session)

            AfinaQuery.execute(query = EXECUTE_GROUP_RATE_LOAN_PHYSIC, sessionSetting = session)
        } catch (e: Exception) {
            logger.error("execute", e)

            AfinaQuery.rollbackFree(session)

            throw SessionException(e.message?:"")
        }
        AfinaQuery.commitFree(session)

        return State.OK
    }

    private const val EXECUTE_GROUP_RATE_LOAN_JURIC = "{ call od.PTKB_PRECEPT.registrationRateLoanJuric(sysdate) }"

    private const val EXECUTE_GROUP_RATE_LOAN_PHYSIC = "{ call od.PTKB_PRECEPT.registrationRateLoanPhysic(sysdate) }"
}