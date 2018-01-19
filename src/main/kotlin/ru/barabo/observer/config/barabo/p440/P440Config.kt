package ru.barabo.observer.config.barabo.p440

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.task.*
import java.util.*

object P440Config: ConfigTask {

    override var timer: Timer? = null

    // override var exitStatus: ExitStatus = ExitStatus.STOP

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

        this.executeTasks()

//        PnoLoader.executeAll()
//        RpoLoader.executeAll()
//        RooLoader.executeAll()
//        ZsnLoader.executeAll()
//        ZsoLoader.executeAll()
//        ZsvLoader.executeAll()
//        ApnLoader.executeAll()
//        ApoLoader.executeAll()
//        ApzLoader.executeAll()
//
//        PbSaver.executeAll()
//        BnpSaver.executeAll()
//        ExistsSaver.executeAll()
//        RestSaver.executeAll()
//        ExtractMainSaver.executeAll()
//
//        ToUncrypto440p.executeAll()
//        ToCrypto440p.executeAll()
//        AddToArchive440p.executeAll()
//        SignArchive440p.executeAll()
//
//        Process440p.executeAll()
//        TryPnoExecute.executeAll()
//        RooWaitCancel.executeAll()
    }
}