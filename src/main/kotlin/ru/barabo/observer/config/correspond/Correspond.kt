package ru.barabo.observer.config.correspond

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.correspond.task.DecryptEdFile

object Correspond  : AbstractConfig() {

    override fun timeOut(): Long = 20_000

    override fun name(): String = "Корр. Счёт"

    override fun configRun() {

        DecryptEdFile.findAll()

        executeTasks()
    }
}