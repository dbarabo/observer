package ru.barabo.observer.config.barabo.crypto.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
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

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(10, 0), LocalTime.of(15, 50), Duration.ofHours(1))

    override fun name(): String = "311-П 1. Запустить выгрузку"

    override fun config(): ConfigTask = PlasticOutSide // CryptoConfig

    private const val EXEC_CREATE_JUR_ACCOUNT = "{ call od.PTKB_FNS_EXPORT_XML.execJurDataPriorDay }"

    private const val EXEC_CREATE_PHYSIC_ACCOUNT = "{ call od.PTKB_FNS_EXPORT_XML.execDataPriorDay }"

    override fun execute(elem: Elem): State {

        var error: String? = null

        try {
            AfinaQuery.execute(EXEC_CREATE_JUR_ACCOUNT)
        } catch (e : SessionException) {

            logger.error("EXEC_CREATE_JUR_ACCOUNT", e)

            error = e.message
        }

        AfinaQuery.execute(EXEC_CREATE_PHYSIC_ACCOUNT)

        error?.let { throw SessionException(it) }

        return State.OK
    }
}