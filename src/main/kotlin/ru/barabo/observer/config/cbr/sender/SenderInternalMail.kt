package ru.barabo.observer.config.cbr.sender

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.sender.task.EmailTempSender

object SenderInternalMail : AbstractConfig() {

    override fun timeOut(): Long = 15_000

    override fun name(): String = "Отправка Внутренней почты"

    override fun configRun() {

        EmailTempSender.findAll()

        executeTasks()
    }
}