package ru.barabo.observer.config.fns.scad

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.barabo.p440.task.Ticket440pFns
import ru.barabo.observer.config.portal.task.GetProcess550pFiles
import ru.barabo.observer.config.fns.scad.task.UncryptoEns
import ru.barabo.observer.config.skad.crypto.task.*

object CryptoScad : AbstractConfig() {

    override fun name(): String = "Крипто-Signature"

    override fun timeOut(): Long = 10_000

    override fun configRun() {

        Uncrypto550pScad.findAll()
        SignScadSend550p.findAll()


         UncryptoEns.findAll()

         Ticket440pFns.findAll()

         Ticket311pFnsLoadScad.findAll()

         UncryptoScad440p.findAll()

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

         UnCryptoScad364p.findAll()
         UnSignScad364p.findAll()
         UnCryptoAny.findAll()

         this.executeTasks()
    }
}