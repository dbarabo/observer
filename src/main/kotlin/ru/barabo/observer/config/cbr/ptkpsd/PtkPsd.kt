package ru.barabo.observer.config.cbr.ptkpsd

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.task.*
import java.util.*

object PtkPsd : ConfigTask {

    override var timer: Timer? = null

    override fun name(): String = "ПТК ПСД"

    override fun timeOut(): Long = 20_000

    override fun configRun() {


        // find all
        SendByPtkPsdNoXml.findAll()
        SendXmlByPtkbPsd.findAll()
        Send364pSign.findAll()
        Send440pArchive.findAll()
        Send311pArchive.findAll()
        SendByPtkPsdCopy.findAll()
        CheckerIsSendPtkPsd.findAll()

        Load101FormXml.findAll()

        CheckerAllBalance.findAll()

        CheckerRedSaldoNow.findAll()
        CheckerDoubleTurnPeriod.findAll()

        // execute all
        executeTasks()
    }
}