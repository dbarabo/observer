package ru.barabo.observer.config.cbr.ticket

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.ticket.task.*

object TicketPtkPsd : AbstractConfig() {

    override fun name(): String = "Квитки по ПТК ПСД"

    override fun timeOut(): Long = 25_000

    override fun configRun() {

        TicketXml.findAll()
        TicketSimple.findAll() // must be last

        XmlLoaderCbrTicket311p.findAll()

        ResponseXlsFor.findAll()

        EfudkoResponseObserver.findAll()

        // execute all
        executeTasks()
    }
}