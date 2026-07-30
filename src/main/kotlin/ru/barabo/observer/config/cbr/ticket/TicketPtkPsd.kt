package ru.barabo.observer.config.cbr.ticket

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.ptkpsd.task.TicketXmlWithoutCab
import ru.barabo.observer.config.cbr.ticket.task.*
import ru.barabo.observer.config.portal.task.UnsignTicket600p
import ru.barabo.observer.config.skad.crypto.task.Uncrypto550pScad

object TicketPtkPsd : AbstractConfig() {

    override fun name(): String = "Квитки по ПТК ПСД"

    override fun timeOut(): Long = 25_000

    override fun configRun() {

        UnsignTicket600p.findAll()

        TicketXml.findAll()

        TicketSimple.findAll()

        // execute all
        executeTasks()
    }
}