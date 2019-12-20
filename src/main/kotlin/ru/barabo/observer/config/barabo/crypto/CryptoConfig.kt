package ru.barabo.observer.config.barabo.crypto

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.crypto.task.*

object CryptoConfig : AbstractConfig() {

    override fun name(): String = "Verba/CryptoPro и Прочее"

    override fun timeOut(): Long = 20_000

    override fun configRun() {

        CryptoNbki.findAll()
        UnCryptoNbki.findAll()

        CreateAccount311p.findAll()
        RecreateAfterError311p.findAll()

        LoadRateThb.findAll()
        FixedExchangeCb.findAll()
        ExecuteExchangeCb.findAll()

        // with starting 16-12-2019 verba off
        // SaveAccount311p.findAll()

        // CryptoExchange.findAll()
        // CryptoLegalization.findAll()

        // Sign390pArchive.findAll()

        //UnCrypto364p.findAll()
        //  UnSign364p.findAll()

        // это тоже надо отрубить, но потом
        CreateSaveResponse390p.findAll()

        // InfoRequest349p.findAll()

        this.executeTasks()
    }


}