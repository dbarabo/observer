package ru.barabo.observer.config.barabo.plastic.turn

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.plastic.FinderHcardIn
import ru.barabo.observer.config.barabo.plastic.turn.task.*

object PlasticTurnConfig : AbstractConfig() {

    override fun name(): String = "Пластик: Обороты"

    override fun timeOut(): Long  = 20_000

    override fun configRun() {

        // include HcardInObject.actionTask
        FinderHcardIn.findAll()

        BinLoader.findAll()

        //OutIbi.findAll()
        OutIbiAll.findAll()

        ExecuteAfp.findAll()
        ExecuteCtlMtl.findAll()
        ExecuteObi.findAll()
        ExecuteClearInt.findAll()
        ExecuteClearIntConverse.findAll()

        IbiSendToJzdo.findAll()
        //OutRestCheck.findAll()
        //OutIbiFakeData.findAll()

        this.executeTasks()
    }
}