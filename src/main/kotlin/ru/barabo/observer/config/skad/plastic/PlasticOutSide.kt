package ru.barabo.observer.config.skad.plastic

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.crypto.task.CreateAccount311p
import ru.barabo.observer.config.barabo.crypto.task.ExecuteExchangeCbr
import ru.barabo.observer.config.barabo.crypto.task.RecreateAfterError311p
import ru.barabo.observer.config.barabo.plastic.release.task.AutoUpdatePlasticJarCritical
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRest
import ru.barabo.observer.config.cbr.other.task.ExecuteReglamentRun
import ru.barabo.observer.config.skad.plastic.task.*

object PlasticOutSide : AbstractConfig() {

    override fun name(): String = "Разное/Извещения"

    override fun timeOut(): Long  = 15_000

    override fun configRun() {
        OutRegisterAquiring.findAll()
        OutRest.findAll()

        CreateAccount311p.findAll()
        RecreateAfterError311p.findAll()

        CbrCurrencyLoader.findAll()
        ExecuteExchangeCbr.findAll()

        AutoUpdatePlasticJarCritical.findAll()

        GitObjects.findAll()

        CheckSpaceServer.findAll()
        CheckTableSpace.findAll()

        FsfmExchangeResponse.findAll()

        MoveTicketNbki.findAll()


        ExecuteReglamentRun.findAll()
        SendXmlForm310.findAll()
        SendXmlRiskClientCbrAuto.findAll()

        UpdaterCrossRateMtl.findAll()

        this.executeTasks()
    }
}