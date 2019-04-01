package ru.barabo.observer.config.barabo.plastic.turn

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.plastic.FinderHcardIn
import ru.barabo.observer.config.barabo.plastic.turn.task.*

object PlasticTurnConfig: AbstractConfig() {

    override fun name(): String = "Пластик: Обороты"

    override fun timeOut(): Long  = 20_000

    override fun configRun() {

        // include HcardInObject.actionTask
        FinderHcardIn.findAll()
        OutIbi.findAll()
        OutIbiAll.findAll()
        ExecuteAfp.findAll()
        ExecuteCtlMtl.findAll()
        ExecuteObi.findAll()
        IbiSendToJzdo.findAll()
        OutRestCheck.findAll()
        OutRegisterAquiring.findAll()

        //OutIbiFakeData.findAll()

        this.executeTasks()
    }
}