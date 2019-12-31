package ru.barabo.observer.config.jzdo.upay

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.jzdo.upay.task.LoadMtlUPay
import ru.barabo.observer.config.jzdo.upay.task.LoadObiUPay
import ru.barabo.observer.config.jzdo.upay.task.SftpLoad
import ru.barabo.observer.config.jzdo.upay.task.UncryptoUPay

object UPayConfig : AbstractConfig() {
    override fun name(): String = "Union Pay"

    override fun timeOut(): Long = 30_000

    override fun configRun() {
        SftpLoad.findAll()

        UncryptoUPay.findAll()

        LoadMtlUPay.findAll()

        LoadObiUPay.findAll()

        executeTasks()
    }
}