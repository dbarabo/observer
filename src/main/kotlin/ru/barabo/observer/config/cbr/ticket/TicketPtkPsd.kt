package ru.barabo.observer.config.cbr.ticket

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.task.*
import java.util.*

object TicketPtkPsd : ConfigTask {

    override var timer: Timer? = null
    // override var exitStatus: ExitStatus = ExitStatus.STOP

    override fun name(): String = "Квитки по ПТК ПСД"

    override fun timeOut(): Long = 25_000

    override fun configRun() {

        // find all
        TicketLegalText.findAll()
        TicketLegalCab.findAll()
        Ticket311pCbr.findAll()
        Ticket311pFns.findAll()
        TicketFtsText.findAll()
        TicketFtsCab.findAll()
        Ticket364FtsText.findAll()
        Ticket364FtsCab.findAll()
        Fsfm349pRequest.findAll()
        TicketFsfm349p.findAll()
        Ticket550p.findAll()
        TicketVbkArchive.findAll()

        TicketXml.findAll()
        TicketSimple.findAll()

        Ticket440pCbr.findAll()
        // TODO Ticket440pFns.findAll()


        // execute all
        TicketLegalText.executeAll()
        TicketLegalCab.executeAll()
        Ticket311pCbr.executeAll()
        Ticket311pFns.executeAll()
        TicketFtsText.executeAll()
        TicketFtsCab.executeAll()
        Ticket364FtsText.executeAll()
        Ticket364FtsCab.executeAll()
        Fsfm349pRequest.executeAll()
        TicketFsfm349p.executeAll()
        Ticket550p.executeAll()
        TicketVbkArchive.executeAll()

        TicketSimple.executeAll()
        TicketXml.executeAll()

        Ticket440pCbr.executeAll()
        // TODO Ticket440pFns.executeAll()
    }
}