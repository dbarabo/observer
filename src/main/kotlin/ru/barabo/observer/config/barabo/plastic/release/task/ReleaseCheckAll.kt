package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object ReleaseCheckAll: Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(7, 0), LocalTime.of(10, 0))

    override fun name(): String = "Пластик Выпуск: Проверить Всё"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override fun execute(elem: Elem): State {
        AfinaQuery.execute(EXEC_CHECK_RELEASE)

        return State.OK
    }

    private const val EXEC_CHECK_RELEASE = "{ call od.PTKB_PLASTIC_AUTO.checkAllPacketState }"
}