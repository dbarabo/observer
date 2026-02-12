package ru.barabo.observer.config.cbr.sender

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.sender.task.EmailTempSender
import ru.barabo.observer.config.skad.acquiring.task.NewMantisChecker
import ru.barabo.observer.config.skad.acquiring.task.RegisterBySchedulerTerminal

object SenderMail : AbstractConfig() {

    override fun timeOut(): Long = 15_000

    override fun name(): String = "Отправка реестров"

    override fun configRun() {

        RegisterBySchedulerTerminal.findAll()

        executeTasks()
    }
}