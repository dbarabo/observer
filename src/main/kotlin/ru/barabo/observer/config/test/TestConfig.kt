package ru.barabo.observer.config.test

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.task.OutRegisterAquiringMonth
import ru.barabo.observer.config.barabo.plastic.release.task.OutRegisterAquiringWeek
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadAcq
import ru.barabo.observer.config.cbr.other.task.LoanInfoSaver
import ru.barabo.observer.config.cbr.ptkpsd.task.SendBlockUnblockAccountPC
import java.util.*

object TestConfig : ConfigTask {
    override var timer: Timer? = null

    override fun name(): String = "TEST!!!"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        //LoadCtlMtl.findAll()
        //ExecuteCtlMtl.findAll()

        //OutRegisterAquiringWeek.findAll()


        //OutRegisterAquiringMonth.findAll()

        //SendBlockUnblockAccountPC.findAll()

        //LoadAcq.findAll()

        this.executeTasks()
    }
}