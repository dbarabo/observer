package ru.barabo.observer.config.fns.monitor

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.fns.monitor.task.MonitorUserOpenDocument

object MonitorConfig : AbstractConfig() {

    override fun name(): String = "Монитор"

    override fun timeOut(): Long = 5_000

    override fun configRun() {

        MonitorUserOpenDocument.findAll()

        this.executeTasks()
    }
}