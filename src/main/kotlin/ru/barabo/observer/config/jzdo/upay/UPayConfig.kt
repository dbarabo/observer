package ru.barabo.observer.config.jzdo.upay

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.jzdo.upay.task.*

object UPayConfig : AbstractConfig() {
    override fun name(): String = "Union Pay"

    override fun timeOut(): Long = 20_000

    override fun configRun() {
        SftpLoad.findAll()

        UncryptoUPay.findAll()

        LoadAcqAdvUPay.findAll()

        LoadMtlUPay.findAll()

        LoadObiUPay.findAll()

        executeTasks()
    }
}