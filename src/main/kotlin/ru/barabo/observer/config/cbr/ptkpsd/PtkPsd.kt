package ru.barabo.observer.config.cbr.ptkpsd

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.task.*
import java.util.*

object PtkPsd : ConfigTask {

    override var timer: Timer? = null

    // override var exitStatus: ExitStatus = ExitStatus.STOP

    override fun name(): String = "ПТК ПСД"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        // find all
        SendByPtkPsdNoXml.findAll()
        SendXmlByPtkbPsd.findAll()
        SendByPtkPsdCopy.findAll()
        Send364pSign.findAll()
        Get440pFiles.findAll()
        CheckerIsSendPtkPsd.findAll()
        GetProcess550pFiles.findAll()
        Send440pArchive.findAll()

        // execute all
        SendByPtkPsdNoXml.executeAll()
        SendXmlByPtkbPsd.executeAll()
        SendByPtkPsdCopy.executeAll()
        Send364pSign.executeAll()
        Get440pFiles.executeAll()
        CheckerIsSendPtkPsd.executeAll()
        GetProcess550pFiles.executeAll()
        Send440pArchive.executeAll()
    }
}