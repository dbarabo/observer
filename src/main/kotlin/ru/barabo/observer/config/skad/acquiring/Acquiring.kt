package ru.barabo.observer.config.skad.acquiring

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.skad.acquiring.task.*

object Acquiring  : AbstractConfig() {
    override fun name(): String = "Эквайринг"

    override fun timeOut(): Long = 10_000

    override fun configRun() {
        ResetSchedulerState.findAll()

        RecalcTerminalsRate.findAll()

        AcquiringProcessTerminal.findAll()

        MinComissionMonthPos.findAll()

        executeTasks()
    }
}