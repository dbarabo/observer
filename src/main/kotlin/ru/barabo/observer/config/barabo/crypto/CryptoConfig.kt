package ru.barabo.observer.config.barabo.crypto

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.crypto.task.*

object CryptoConfig : AbstractConfig() {

    override fun name(): String = "Verba/CryptoPro и Прочее"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        UnCrypto364p.findAll()
        UnSign364p.findAll()
        CryptoNbki.findAll()
        UnCryptoNbki.findAll()
        CreateAccount311p.findAll()
        SaveAccount311p.findAll()
        InfoRequest349p.findAll()
        LoadRateThb.findAll()
        CryptoExchange.findAll()
        CryptoLegalization.findAll()

        CreateSaveResponse390p.findAll()
        Sign390pArchive.findAll()

        this.executeTasks()
    }


}