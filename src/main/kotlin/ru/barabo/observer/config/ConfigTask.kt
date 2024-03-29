package ru.barabo.observer.config

import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.derby.StoreSimple
import java.time.ZoneId
import java.util.*

/**
 * Описание конфигурации
 */
interface ConfigTask {

    var timer: Timer?

    fun name(): String

    fun configRun()

    fun timeOut(): Long

    fun starting()

    fun isRun(): Boolean = timer != null

    fun stoping() {

       timer?.cancel()
       timer?.purge()

       timer = null
    }

    fun executeTasks() {

       val items = StoreSimple
           .getItems { it?.config() == this }
           .filter { (it.task as? Executor)?.isAccess() == true }
           .sortedBy { it.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: Long.MAX_VALUE }


        for (item in items) {

            (item.task as? Executor)?.executeElem(item)
        }
    }
}