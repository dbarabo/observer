package ru.barabo.observer.config.cbr.ticket

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.task.ResponseXlsFor
import ru.barabo.observer.config.cbr.ticket.task.TicketSimple
import ru.barabo.observer.config.cbr.ticket.task.TicketXml
import java.util.*

object TicketPtkPsd : ConfigTask {

    override var timer: Timer? = null

    override fun name(): String = "Квитки по ПТК ПСД"

    override fun timeOut(): Long = 25_000

    override fun configRun() {

        TicketXml.findAll()
        TicketSimple.findAll() // must be last

        ResponseXlsFor.findAll()

        // execute all
        executeTasks()
    }
}