package ru.barabo.observer.config.test

import ru.barabo.observer.config.AbstractConfig

object TestConfig : AbstractConfig() {

    override fun name(): String = "TEST!!!"

    override fun timeOut(): Long = 5_000

    override fun configRun() {

        // LoadPaymentWeechatXlsx.findAll()

        this.executeTasks()
    }
}