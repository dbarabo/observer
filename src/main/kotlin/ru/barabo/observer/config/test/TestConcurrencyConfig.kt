package ru.barabo.observer.config.test

import ru.barabo.observer.config.AbstractConfig

object TestConcurrencyConfig : AbstractConfig() {

    override fun name(): String = "TEST-CONCURRENCY!!!"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        //LoadCtlMtl.findAll()
        //ExecuteCtlMtl.findAll()

        //OutRegisterAquiringWeek.findAll()


        //OutRegisterAquiringMonth.findAll()

        //SendBlockUnblockAccountPC.findAll()

        //LoadAcq.findAll()

        Thread.sleep(1000*60*2)

        this.executeTasks()
    }
}