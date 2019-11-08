package ru.barabo.observer.config.barabo.p440

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.*

object P440Config: AbstractConfig() {

    override fun name(): String = "440-П"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        PnoLoader.findAll() // add all 440p files without ticket KWT & IZV
        PbSaver.findAll() // add all 440p records to out

        Process440p.findAll()
        TryPnoExecute.findAll()
        RooWaitCancel.findAll()

        ToUncrypto440p.findAll()
        ToCrypto440p.findAll()
        AddToArchive440p.findAll()
        SignArchive440p.findAll()

        Ticket440pCbr.findAll()
        Ticket440pFns.findAll()

        // TEST SCAD!!!
        // UncryptoScad440p.findAll()
        // CryptoScad440p.findAll()

        this.executeTasks()
    }
}