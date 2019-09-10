package ru.barabo.observer.config.cbr.correspondent.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.correspondent.Correspondent
import ru.barabo.observer.config.cbr.turncard.TurnCard
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object SleepTest : Periodical {
    override fun name(): String = "SleepTest"

    override val accessibleData: AccessibleData = AccessibleData()

    override fun config(): ConfigTask = Correspondent

    override val unit: ChronoUnit = ChronoUnit.SECONDS

    override var count: Long = 30

    override var lastPeriod: LocalDateTime? = null


    override fun execute(elem: Elem): State {
        logger.error("SleepTest START")

        AfinaQuery.execute(EXEC_SLEEP)

        logger.error("SleepTest END")

        return State.OK
    }



    private const val EXEC_SLEEP = "{ call od.PTKB_PLASTIC_TURNOUT.SLEEP_TEST }"

    private val logger = LoggerFactory.getLogger(SleepTest::class.java)

}