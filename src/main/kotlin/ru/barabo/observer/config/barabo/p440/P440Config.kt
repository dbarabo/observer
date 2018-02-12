package ru.barabo.observer.config.barabo.p440

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.task.*
import java.util.*

object P440Config: ConfigTask {

    override var timer: Timer? = null

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

        this.executeTasks()
    }
}