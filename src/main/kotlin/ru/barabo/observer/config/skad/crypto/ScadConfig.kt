package ru.barabo.observer.config.skad.crypto

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.Ticket440pCbr
import ru.barabo.observer.config.barabo.p440.task.Ticket440pFns
import ru.barabo.observer.config.skad.crypto.task.*

object ScadConfig : AbstractConfig() {

    override fun name(): String = "SCAD Signature Крипто"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        SaveAndCryptoScad311p.findAll()
        SignScadArchive311p.findAll()

        UncryptoScad440p.findAll()
        CryptoScad440p.findAll()
        AddToArchive440pScad.findAll()
        PbSaverScad.findAll()
        SignScadArchive440p.findAll()

        Ticket440pCbr.findAll()
        Ticket440pFns.findAll()

        CryptoFtsValScad.findAll()
        CryptoValCb181UScad.findAll()
        SignScadOnlyFile.findAll()
        SignScadCbFts181U.findAll()
        UnCryptoScad364p.findAll()
        UnSignScad364p.findAll()

        Uncrypto550pScad.findAll()
        SignScadSend550p.findAll()


        executeTasks()
    }
}