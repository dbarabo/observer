package ru.barabo.observer.config.cbr.other

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.cbr.ptkpsd.task.ClearPrimFromArchiveDay
import ru.barabo.observer.config.skad.crypto.task.SignScad181UByCbr

object OtherCbr: AbstractConfig() {

    override fun timeOut(): Long = 30_000

    override fun name() :String = "Прочее"

    override fun configRun() {

        SignScad181UByCbr.findAll()

        RemartMail.findAll()
        TtsMailFromOtk.findAll()
        SbMailFromCbr.findAll()

        NbkiAllReportsSend.findAll()
        UnlockUsersMonday.findAll()

        CecReportProcess.findAll()
        CheckTicket440p.findAll()
        CheckTicketArchive440p.findAll()

        CheckTicketArchive311p.findAll()
        CheckTicketCbrFile311p.findAll()
        CheckTicketFns311p.findAll()

        CheckOpenArchiveDay.findAll()
        ExecOpenArchiveDay.findAll()
        RestrictUsersArchiveDay.findAll()
        CheckActializationEntryDouble.findAll()

        // off 06.04.2020 go to week reports
        CorrectPrim.findAll()
        CorrectPrimMonth.findAll()

        ExecuteReglamentRun.findAll()

        ExecuteGroupRateLoan.findAll()

        CorrDepartmentGoHome.findAll()

        // SendBlockUnblockAccountPC.findAll()

        ClearPrimFromArchiveDay.findAll()

        ReportSender.findAll()

        MantisUserDisabled.findAll()

        executeTasks()
    }

}