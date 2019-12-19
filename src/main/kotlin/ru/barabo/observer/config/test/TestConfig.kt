package ru.barabo.observer.config.test

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.crypto.task.ExecExchangeCb

object TestConfig : AbstractConfig() {

    override fun name(): String = "TEST!!!"

    override fun timeOut(): Long = 5_000

    override fun configRun() {

        //SftpLoad.findAll()

        //LoadCtlMtl.findAll()
        //ExecuteCtlMtl.findAll()

        //OutRegisterAquiringWeek.findAll()


        //OutRegisterAquiringMonth.findAll()

        //SendBlockUnblockAccountPC.findAll()

        //LoadAcq.findAll()

        // ExecExchangeCb.findAll()

        this.executeTasks()
    }
}