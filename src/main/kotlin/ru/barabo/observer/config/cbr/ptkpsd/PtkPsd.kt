package ru.barabo.observer.config.cbr.ptkpsd

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.Ticket440pCbr
import ru.barabo.observer.config.cbr.ptkpsd.task.*

object PtkPsd : AbstractConfig() {

    override fun name(): String = "ПТК ПСД"

    override fun timeOut(): Long = 20_000

    override fun configRun() {


        // find all
        SendByPtkPsdNoXml.findAll()
        SendXmlByPtkbPsd.findAll()
        Send440pArchive.findAll()
        Send311pArchive.findAll()
        SendByPtkPsdCopy.findAll()
        CheckerIsSendPtkPsd.findAll()

        ClearPrimFromArchiveDay.findAll()

        Load101FormXml.findAll()

        CheckerRedSaldoNow.findAll()
        CheckerDoubleTurnPeriod.findAll()

        // Send364pSign.findAll()

        Ticket440pCbr.findAll()

        // execute all
        executeTasks()
    }
}