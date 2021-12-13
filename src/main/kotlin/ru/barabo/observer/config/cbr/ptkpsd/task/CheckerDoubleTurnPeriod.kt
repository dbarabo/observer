package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.task.form101.CheckerDoubleTurn
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CheckerDoubleTurnPeriod : Periodical {

    override val unit: ChronoUnit = ChronoUnit.HOURS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData =
            AccessibleData(workTimeFrom = LocalTime.of(8, 15), workTimeTo = LocalTime.of(20, 0))

    override fun name(): String = "Проверка задвоения проводок"

    override fun config(): ConfigTask = AnyWork // PtkPsd

    override fun execute(elem: Elem): State {
        CheckerDoubleTurn.checkDouble()

        return State.OK
    }
}