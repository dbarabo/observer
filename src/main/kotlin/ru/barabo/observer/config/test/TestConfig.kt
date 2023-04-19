package ru.barabo.observer.config.test

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.GetSmevArchives

object TestConfig : AbstractConfig() {

    override fun name(): String = "TEST!!!"

    override fun timeOut(): Long = 5_000

    override fun configRun() {

        //ClientRiskLoader.findAll()
        GetSmevArchives.findAll()

        this.executeTasks()
    }
}