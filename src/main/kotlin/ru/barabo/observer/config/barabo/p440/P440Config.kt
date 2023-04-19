package ru.barabo.observer.config.barabo.p440

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.*
import ru.barabo.observer.config.skad.crypto.task.AddToArchive440pScad

object P440Config: AbstractConfig() {

    override fun name(): String = "440-П"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        PnoLoader.findAll() // add all 440p files without ticket KWT & IZV

        Process440p.findAll()
        TryPnoExecute.findAll()
        RooWaitCancel.findAll()
        BosSendIfNeed.findAll()

        AddToArchive440pScad.findAll()

        //GetSmevArchives.findAll()

        this.executeTasks()
    }
}