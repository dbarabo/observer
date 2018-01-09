package ru.barabo.observer.config.barabo.crypto.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CreateAccount311p : Periodical {

    private val logger = LoggerFactory.getLogger(CreateAccount311p::class.java)

    override val unit: ChronoUnit =  ChronoUnit.DAYS

    override val count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(9, 0), LocalTime.of(16, 0), Duration.ofHours(1))

    override fun name(): String = "311-П 1. Запустить выгрузку"

    override fun config(): ConfigTask = CryptoConfig

    private val EXEC_CREATE_JUR_ACCOUNT = "{call od.PTKB_EXEC_FILL_ACCOUNT_JUR}"

    private val EXEC_CREATE_PHYSIC_ACCOUNT = "{call od.PTKB_EXEC_FILL_ACCOUNT_PHYS}"

    override fun execute(elem: Elem): State {

        var error :String? = null

        try {
            AfinaQuery.execute(EXEC_CREATE_JUR_ACCOUNT)
        } catch (e : SessionException) {

            logger.error("EXEC_CREATE_JUR_ACCOUNT", e)

            error = e.message
        }

        AfinaQuery.execute(EXEC_CREATE_PHYSIC_ACCOUNT)

        error?.let { SessionException(it) }

        return State.OK
    }
}