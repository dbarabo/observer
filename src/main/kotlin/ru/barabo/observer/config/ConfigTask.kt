package ru.barabo.observer.config

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.cbr.turncard.task.TurnOutOnline
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.derby.StoreSimple
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

       val items = StoreSimple.getItems { it?.config() == this }
               .filter { (it.task as? Executor)?.isAccess() == true }

        for (item in items) {

            (item.task as? Executor)?.executeElem(item)
        }
    }
}