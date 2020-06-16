package ru.barabo.observer.config.cbr.turncard

import ru.barabo.observer.config.NoneCheckedConfig
import ru.barabo.observer.config.cbr.turncard.task.TurnOutOnline
import ru.barabo.observer.config.cbr.turncard.task.TurnOutTechOver

object TurnCard : NoneCheckedConfig() {

    override fun timeOut(): Long = 10_000

    override fun name(): String = "Выгрузка оборотов в ПЦ"

    override fun configRun() {
       // TurnOutOnline.processTurnCard()

        TurnOutOnline.findAll()
        TurnOutDeleted.findAll()
        TurnOutTechOver.findAll()

        executeTasks()
    }
}
