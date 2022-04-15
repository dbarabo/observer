package ru.barabo.observer.config.test

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.skad.anywork.task.ClientRiskLoader

object TestConfig : AbstractConfig() {

    override fun name(): String = "TEST!!!"

    override fun timeOut(): Long = 5_000

    override fun configRun() {

        ClientRiskLoader.findAll()
        // LoadPaymentWeechatXlsx.findAll()

        this.executeTasks()
    }
}