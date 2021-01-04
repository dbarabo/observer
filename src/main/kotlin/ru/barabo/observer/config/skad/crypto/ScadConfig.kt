package ru.barabo.observer.config.skad.crypto

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.crypto.task.CreateSaveResponse390p
import ru.barabo.observer.config.barabo.crypto.task.InfoRequest349p
import ru.barabo.observer.config.barabo.crypto.task.Sign390pArchive
import ru.barabo.observer.config.barabo.p440.task.Ticket440pFns
import ru.barabo.observer.config.skad.crypto.task.*

object ScadConfig : AbstractConfig() {

    override fun name(): String = "SCAD Signature Крипто"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        // CreateCrypto311p512 - replace SaveAndCryptoScad311p from 01/01/2021
        CreateCrypto311p512.findAll()
        //SaveAndCryptoScad311p.findAll()

        SignScadArchive311p.findAll()
        Ticket311pFnsLoadScad.findAll()

        UncryptoScad440p.findAll()
        CryptoScad440p.findAll()
        PbSaverScad.findAll()
        SignScadArchive440p.findAll()

        Ticket440pFns.findAll()

        CryptoFtsValScad.findAll()
        CryptoValCb181UScad.findAll()
        SignScadOnlyFile.findAll()
        SignScadCbFts181U.findAll()
        UnCryptoScad364p.findAll()
        UnSignScad364p.findAll()
        UnCryptoAny.findAll()

        Uncrypto550pScad.findAll()
        SignScadSend550p.findAll()

        InfoRequest349p.findAll()

        Crypto4077UScad.findAll()
        AddSign600P.findAll()
        CryptoArchive600P.findAll()

        AddSignMain600P.findAll()

        CreateSaveResponse390p.findAll()
        Sign390pArchive.findAll()

        executeTasks()
    }
}