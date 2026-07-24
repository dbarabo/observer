package ru.barabo.observer.config.portal

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.portal.task.GetProcess550pFiles
import ru.barabo.observer.config.portal.task.Send364pArchiveKesdt
import ru.barabo.observer.config.portal.task.Send364pArchivePseiDsei
import ru.barabo.observer.config.portal.task.Ticket364FtsCab
import ru.barabo.observer.config.portal.task.Ticket364FtsText
import ru.barabo.observer.config.portal.task.Ticket550p

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

        this.executeTasks()
    }
}