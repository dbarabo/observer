package ru.barabo.observer.config.barabo.plastic.release

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.task.*
import java.util.*

object PlasticReleaseConfig: ConfigTask {

    override var timer: Timer? = null

    override fun name(): String = "Пластик: Выпуск/Перевыпуск"

    override fun timeOut(): Long = 20_000

    override fun configRun() {
        SendToJzdo.findAll()
        GetFromJzdo.findAll()
        IvrSendRequest.findAll()
        IvrGetResponse.findAll()
        GetIiaAccept.findAll()

        this.executeTasks()
    }

}