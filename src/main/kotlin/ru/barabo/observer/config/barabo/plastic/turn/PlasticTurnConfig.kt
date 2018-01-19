package ru.barabo.observer.config.barabo.plastic.turn

import ru.barabo.observer.config.ConfigTask
import java.util.*

object PlasticTurnConfig: ConfigTask {
    override var timer: Timer?  = null

    override fun name(): String = "Пластик: Обороты"

    override fun timeOut(): Long  = 20_000

    override fun configRun() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}