package ru.barabo.observer.config

import java.util.*
import kotlin.concurrent.timer

abstract class NoneCheckedConfig : ConfigTask {

    override var timer: Timer? = null

    override fun starting() {

        startConfig()
    }

    private fun startConfig() {

        timer = timer(name = this.javaClass.simpleName, initialDelay = 5_000, daemon = false, period = timeOut()) {

            configRun()
        }
    }
}