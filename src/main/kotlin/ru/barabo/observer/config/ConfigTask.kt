package ru.barabo.observer.config

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeoutOrNull
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * Описание конфигурации
 */
interface ConfigTask {

    var exitStatus :ExitStatus

    fun name() :String

    fun configRun()

    fun timeOut() :Long

    fun starting() {
        synchronized(exitStatus) {
            if(exitStatus == ExitStatus.RUN) return

            exitStatus = ExitStatus.RUN
        }
        startConfig()
    }

    fun isRun() :Boolean = synchronized(exitStatus) {exitStatus == ExitStatus.RUN}

    fun stoping() {
        synchronized(exitStatus) {exitStatus = ExitStatus.STOPPING }
    }

    private fun startConfig() {
        thread(name = this.javaClass.simpleName) {
            runBlocking {
                var time :Long = timeOut()
                while( synchronized(exitStatus) {exitStatus == ExitStatus.RUN} ) {

                    //LoggerFactory.getLogger(ConfigTask::class.java).info("CONFIG IS START ${name()}")

                    delay(if(timeOut() > time) timeOut() - time else 100)

                    time = measureTimeMillis {
                        withTimeoutOrNull( timeOut()) { configRun() }
                    }
                }

                synchronized(exitStatus) {exitStatus = ExitStatus.STOP}

                LoggerFactory.getLogger(ConfigTask::class.java).info("IS_STOPED ${name()}")
            }
        }
    }
}