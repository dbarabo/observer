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