package ru.barabo.observer.config.fns.inn

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.fns.inn.task.InnFiller

object InnConfig : AbstractConfig() {

    override fun name(): String = "ИНН ФНС"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        InnFiller.findAll()

        this.executeTasks()
    }
}