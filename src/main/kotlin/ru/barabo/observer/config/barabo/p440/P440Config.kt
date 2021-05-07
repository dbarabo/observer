package ru.barabo.observer.config.barabo.p440

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.PnoLoader
import ru.barabo.observer.config.barabo.p440.task.Process440p
import ru.barabo.observer.config.barabo.p440.task.RooWaitCancel
import ru.barabo.observer.config.barabo.p440.task.TryPnoExecute
import ru.barabo.observer.config.skad.crypto.task.AddToArchive440pScad

object P440Config: AbstractConfig() {

    override fun name(): String = "440-П"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        PnoLoader.findAll() // add all 440p files without ticket KWT & IZV

        Process440p.findAll()
        TryPnoExecute.findAll()
        RooWaitCancel.findAll()

        AddToArchive440pScad.findAll()

        this.executeTasks()
    }
}