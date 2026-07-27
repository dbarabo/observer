package ru.barabo.observer.config.portal

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.portal.task.*

object PortalTransport  : AbstractConfig() {
    override fun name(): String = "Транспорт Portal5"

    override fun timeOut(): Long = 15_000

    override fun configRun() {

        GetProcess550pFiles.findAll()
        Ticket550p.findAll()
        Ticket364FtsCab.findAll()
        Ticket364FtsText.findAll()
        Send364pArchiveKesdt.findAll()
        Send364pArchivePseiDsei.findAll()

        TzFtsArj.findAll()
        TicketFtsText.findAll()

        Ticket600pOnDifm.findAll()
        Ticket407XmlFromCbr.findAll()

        this.executeTasks()
    }
}