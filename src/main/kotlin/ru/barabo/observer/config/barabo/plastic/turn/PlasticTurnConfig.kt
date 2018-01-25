package ru.barabo.observer.config.barabo.plastic.turn

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.*
import java.util.*

object PlasticTurnConfig: ConfigTask {
    override var timer: Timer?  = null

    override fun name(): String = "Пластик: Обороты"

    override fun timeOut(): Long  = 20_000

    override fun configRun() {

        LoadAfp.findAll()
        LoadCtlMtl.findAll()
        LoadObi.findAll()
        LoadObr.findAll()
        LoadRestAccount.findAll()
        OutIbi.findAll()
        OutIbiAll.findAll()
        ExecuteCtlMtl.findAll()
        ExecuteObi.findAll()
        IbiSendToJzdo.findAll()

        this.executeTasks()
    }
}