package ru.barabo.observer.config.fns.scad

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.fns.scad.task.UncryptoEns
import ru.barabo.observer.config.skad.crypto.task.PbSaverScadVer4
import ru.barabo.observer.config.skad.crypto.task.SignScadArchive440p

object CryptoScad : AbstractConfig() {

    override fun name(): String = "Крипто-Signature"

    override fun timeOut(): Long = 10_000

    override fun configRun() {

        UncryptoEns.findAll()

        PbSaverScadVer4.findAll()

        SignScadArchive440p.findAll()

        this.executeTasks()
    }
}