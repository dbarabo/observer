package ru.barabo.observer.config

import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.TaskMapper
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.timer

abstract class AbstractConfig : ConfigTask {

    override var timer: Timer? = null

    override fun starting() {

        startConfig()
    }

    private fun startConfig() {

        timer = timer(name = this.javaClass.simpleName, initialDelay = 10_000, daemon = false, period = timeOut()) {

            checkWorkConfig()

            configRun()
        }
    }

    private var lastWorkTime: AtomicLong = AtomicLong(System.currentTimeMillis())

    fun checkWorkConfig() {

        val now = System.currentTimeMillis()

        lastWorkTime.set(now)

        val nowWaitTime = now - MAX_WAIT_TIME

        TaskMapper.buildInfo.configs
                .filter { ((it as? AbstractConfig)?.lastWorkTime?.get() ?: now) < nowWaitTime }
                .forEach { sendErrorConfigNotRespond(it as AbstractConfig)  }
    }

    private fun sendErrorConfigNotRespond(config: AbstractConfig) {

        val minutes = Duration.ofMillis(System.currentTimeMillis() - config.lastWorkTime.get() ).toMinutes()

        config.lastWorkTime.set(System.currentTimeMillis())

        val subjBody = subjectNotRespond(config.name(), minutes)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = subjBody, body = subjBody)
    }

    private fun subjectNotRespond(nameThread: String, minutes: Long) =
            "Похоже тред с именем $nameThread завис, не отвечает уже более $minutes минут"
}

private const val MAX_WAIT_TIME: Long = 1000*60*21