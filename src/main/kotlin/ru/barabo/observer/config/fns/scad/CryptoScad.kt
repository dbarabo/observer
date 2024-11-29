package ru.barabo.observer.config.fns.scad

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.crypto.task.InfoRequest349p
import ru.barabo.observer.config.barabo.p440.task.Ticket440pFns
import ru.barabo.observer.config.fns.scad.task.UncryptoEns
import ru.barabo.observer.config.skad.crypto.task.*

object CryptoScad : AbstractConfig() {

    override fun name(): String = "Крипто-Signature"

    override fun timeOut(): Long = 10_000

    override fun configRun() {

         UncryptoEns.findAll()

         Ticket440pFns.findAll()

         Ticket311pFnsLoadScad.findAll()

         Uncrypto550pScad.findAll()

         CreateCrypto311p512.findAll()
         SignScadArchive311p.findAll()

         UncryptoScad440p.findAll()

         InfoRequest349p.findAll()

         Crypto4077UScad.findAll()
         AddSign600P.findAll()
         CryptoArchive600P.findAll()
         AddSignMain600P.findAll()

         PbSaverScadVer4.findAll()
         SignScadArchive440p.findAll()

         CryptoScad440p.findAll()

         CryptoFtsValScad.findAll()
         CryptoValCb181UScad.findAll()
         SignScadOnlyFile.findAll()
         SignScadCbFts181U.findAll()

         SignScadSend550p.findAll()

         UnCryptoScad364p.findAll()
         UnSignScad364p.findAll()
         UnCryptoAny.findAll()

         this.executeTasks()
    }
}