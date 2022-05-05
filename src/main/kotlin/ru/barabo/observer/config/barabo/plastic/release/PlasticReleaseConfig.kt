package ru.barabo.observer.config.barabo.plastic.release

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.plastic.release.task.*
import ru.barabo.observer.config.skad.acquiring.task.NewMantisChecker

object PlasticReleaseConfig: AbstractConfig() {

    override fun name(): String = "Пластик: Выпуск/Перевыпуск"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        SendToJzdo.findAll()
        GetFromJzdo.findAll()
        NewMantisChecker.findAll()
        //IvrSendRequest.findAll()
        //IvrGetResponse.findAll()
        //CheckWaitOci.findAll()
        //OutSmsData.findAll()
        //ReleaseCheckAll.findAll()

        // also included HcardInObject.actionTask for GetIiaAccept GetOiaConfirm GetOciData see from PlasticTurnConfig
        this.executeTasks()
    }

}