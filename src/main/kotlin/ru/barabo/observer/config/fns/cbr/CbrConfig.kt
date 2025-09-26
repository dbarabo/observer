package ru.barabo.observer.config.fns.cbr

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.fns.cbr.extract.PbSaverCbr
import ru.barabo.observer.config.fns.cbr.task.ArchiveCloseer
import ru.barabo.observer.config.fns.cbr.task.ArchiveCreator
import ru.barabo.observer.config.fns.cbr.task.LoaderRequest
import ru.barabo.observer.config.fns.cbr.task.ProcessCbrRequest

object CbrConfig : AbstractConfig() {

    override fun name(): String = "ЦБ-Выписки"

    override fun timeOut(): Long = 7_000

    override fun configRun() {

        LoaderRequest.findAll()

        ProcessCbrRequest.findAll()

        PbSaverCbr.findAll()

        ArchiveCreator.findAll()

        ArchiveCloseer.findAll()

        this.executeTasks()
    }
}