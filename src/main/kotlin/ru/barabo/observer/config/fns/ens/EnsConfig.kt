package ru.barabo.observer.config.fns.ens

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.GetSmevArchives
import ru.barabo.observer.config.barabo.p440.task.Process440p
import ru.barabo.observer.config.barabo.p440.task.UnoLoader
import ru.barabo.observer.config.fns.ens.task.Send440pArchiveToSmev
import ru.barabo.observer.config.skad.crypto.task.AddToArchive440pScad

object EnsConfig : AbstractConfig() {

    override fun name(): String = "ЕНС-440П"

    override fun timeOut(): Long = 5_000

    override fun configRun() {

        GetSmevArchives.findAll()

        UnoLoader.findAll() // add all 440p files without ticket KWT & IZV

        Process440p.findAll()

        AddToArchive440pScad.findAll()

        Send440pArchiveToSmev.findAll()

        this.executeTasks()
    }
}