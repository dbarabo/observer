package ru.barabo.observer.config.skad.anywork.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutDfCreator
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object GutDfCreateFile : Periodical {

    override var lastPeriod: LocalDateTime? = null

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override val accessibleData: AccessibleData =  AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(10, 0), LocalTime.of(21, 0),
        Duration.ofMinutes(119))

    override fun name(): String = "GUTDF создать XML"

    override fun config(): ConfigTask = AnyWork

    override fun execute(elem: Elem): State {

        GutDfCreator.createFileByRutdf(null)

        return State.OK
    }
}