package ru.barabo.observer.config.skad.acquiring

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.skad.acquiring.task.AcquiringProcessTerminal

object Acquiring  : AbstractConfig() {
    override fun name(): String = "Эквайринг"

    override fun timeOut(): Long = 18_000

    override fun configRun() {
        AcquiringProcessTerminal.findAll()

        executeTasks()
    }
}