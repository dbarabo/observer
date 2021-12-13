package ru.barabo.observer.config.cbr.other

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.skad.crypto.task.SignScad181UByCbr

object OtherCbr: AbstractConfig() {

    override fun timeOut(): Long = 30_000

    override fun name() :String = "Прочее"

    override fun configRun() {

        SignScad181UByCbr.findAll()

        RemartMail.findAll()
        TtsMailFromOtk.findAll()
        SbMailFromCbr.findAll()

        UnlockUsersMonday.findAll()

        CheckTicket440p.findAll()
        CheckTicketArchive440p.findAll()

        CheckTicketArchive311p.findAll()
        CheckTicketCbrFile311p.findAll()
        CheckTicketFns311p.findAll()

        // off 06.04.2020 go to week reports
        CorrectPrim.findAll()
        CorrectPrimMonth.findAll()

        ReportSender.findAll()

        executeTasks()
    }

}