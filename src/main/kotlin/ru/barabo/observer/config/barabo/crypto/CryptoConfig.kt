package ru.barabo.observer.config.barabo.crypto

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.ExitStatus
import ru.barabo.observer.config.barabo.crypto.task.*

object CryptoConfig : ConfigTask {

    override var exitStatus: ExitStatus = ExitStatus.STOP

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

        UnCrypto364p.executeAll()
        UnSign364p.executeAll()
        CryptoNbki.executeAll()
        UnCryptoNbki.executeAll()
        CreateAccount311p.executeAll()
        SaveAccount311p.executeAll()
        InfoRequest349p.executeAll()
        LoadRateThb.executeAll()
    }


}