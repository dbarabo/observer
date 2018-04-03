package ru.barabo.observer.config.barabo.crypto

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.task.*
import java.util.*

object CryptoConfig : ConfigTask {

    override var timer: Timer? = null

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
        LoadBik.findAll()
        CryptoExchange.findAll()
        CryptoLegalization.findAll()

        this.executeTasks()
    }


}