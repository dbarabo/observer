package ru.barabo.observer.config.test

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.jzdo.upay.task.LoadAcqAdvUPay
import ru.barabo.observer.config.skad.acquiring.task.LoadPaymentWeechatXlsx

object TestConfig : AbstractConfig() {

    override fun name(): String = "TEST!!!"

    override fun timeOut(): Long = 5_000

    override fun configRun() {

        LoadPaymentWeechatXlsx.findAll()

        this.executeTasks()
    }
}