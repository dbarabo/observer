package ru.barabo.observer.config

import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.derby.StoreDerby
import java.util.*
import kotlin.concurrent.timer

/**
 * Описание конфигурации
 */
interface ConfigTask {

    var timer : Timer?

    fun name() :String

    fun configRun()

    fun timeOut() :Long

    fun starting() {

        startConfig()
    }

    fun isRun() :Boolean = timer != null

    fun stoping() {

       timer?.cancel()
       timer?.purge()

       timer = null
    }

    private fun startConfig() {

        timer = timer(name = this.javaClass.simpleName, initialDelay = 10_000, daemon = false, period = timeOut()) { configRun() }
    }

    fun executeTasks() {

       val items = StoreDerby.getItems() {it?.config() == this}

        for (item in items) {

            (item.task as? Executor)?.executeElem(item)
        }

                //.forEach {  it.task?.execute(it) }
    }
}