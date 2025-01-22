package ru.barabo.observer.config.skad.longtime

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.skad.anywork.task.ClientRiskLoader

object LongTime : AbstractConfig() {

    override fun timeOut(): Long = 20_000

    override fun name() :String = "Длительные действия"

    override fun configRun() {
        ClientRiskLoader.findAll()

        executeTasks()
    }
}