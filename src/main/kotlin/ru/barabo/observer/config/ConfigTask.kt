package ru.barabo.observer.config

import java.util.*
import kotlin.concurrent.timer

/**
 * Описание конфигурации
 */
interface ConfigTask {

    //var exitStatus :ExitStatus

    var timer : Timer?

    fun name() :String

    fun configRun()

    fun timeOut() :Long

    fun starting() {
//        synchronized(exitStatus) {
//            if(exitStatus == ExitStatus.RUN) return
//
//            exitStatus = ExitStatus.RUN
//        }
        startConfig()
    }

    fun isRun() :Boolean = timer != null
    //synchronized(exitStatus) {exitStatus == ExitStatus.RUN}

    fun stoping() {

       timer?.cancel()
       timer?.purge()

       timer = null

       // synchronized(exitStatus) {exitStatus = ExitStatus.STOPPING }
    }

    private fun startConfig() {

        timer = timer(name = this.javaClass.simpleName, daemon = false, period = timeOut()) { configRun() }

//        thread(name = this.javaClass.simpleName) {
//            runBlocking {
//                var time :Long = timeOut()
//                while( synchronized(exitStatus) {exitStatus == ExitStatus.RUN} ) {
//
//                    //LoggerFactory.getLogger(ConfigTask::class.java).info("CONFIG IS START ${name()}")
//
//                    delay(if(timeOut() > time) timeOut() - time else 500)
//
//                    time = measureTimeMillis {
//                        withTimeoutOrNull( timeOut()) { configRun() }
//                    }
//                }
//
//                synchronized(exitStatus) {exitStatus = ExitStatus.STOP}
//
//                LoggerFactory.getLogger(ConfigTask::class.java).info("IS_STOPED ${name()}")
//            }
//        }
    }
}