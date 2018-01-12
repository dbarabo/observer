package ru.barabo.observer.config.barabo.p440

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.ExitStatus
import ru.barabo.observer.config.barabo.p440.task.*

object P440Config: ConfigTask {

    override var exitStatus: ExitStatus = ExitStatus.STOP

    override fun name(): String = "440-П"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        PnoLoader.findAll() // add all 440p files without ticket KWT & IZV

        PnoLoader.executeAll()
        RpoLoader.executeAll()
        RooLoader.executeAll()
        ZsnLoader.executeAll()
        ZsoLoader.executeAll()
        ZsvLoader.executeAll()
        ApnLoader.executeAll()
        ApoLoader.executeAll()
        ApzLoader.executeAll()
    }
}