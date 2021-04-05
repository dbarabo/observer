package ru.barabo.observer.config.skad.plastic

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.crypto.task.*
import ru.barabo.observer.config.barabo.plastic.release.task.AutoUpdatePlasticJarCritical
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRest
import ru.barabo.observer.config.skad.plastic.task.*

object PlasticOutSide  : AbstractConfig() {

    override fun name(): String = "Разное/Извещения"

    override fun timeOut(): Long  = 20_000

    override fun configRun() {
        OutRegisterAquiring.findAll()
        OutRest.findAll()

        CreateAccount311p.findAll()
        RecreateAfterError311p.findAll()

        MoveRateCbrToday.findAll()
        LoadRateThb.findAll()
        FixedExchangeCb.findAll()
        ExecuteExchangeCb.findAll()

        AutoUpdatePlasticJarCritical.findAll()

        LoadVisaRate.findAll()
        UpdaterCrossRateMtl.findAll()

        GitObjects.findAll()

        CheckSpaceServer.findAll()
        CheckTableSpace.findAll()

        FsfmExchangeResponse.findAll()

        MoveTicketNbki.findAll()

        this.executeTasks()
    }
}