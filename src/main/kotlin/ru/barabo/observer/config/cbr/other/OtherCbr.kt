package ru.barabo.observer.config.cbr.other

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.ibank.task.LoanInfoCreator
import ru.barabo.observer.config.cbr.ibank.task.LoanInfoSaver
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.cbr.ptkpsd.task.ClearPrimFromArchiveDay
import ru.barabo.observer.config.cbr.ptkpsd.task.SendBlockUnblockAccountPC

object OtherCbr: AbstractConfig() {

    override fun timeOut(): Long = 30_000

    override fun name() :String = "Прочее"

    override fun configRun() {

        RemartMail.findAll()
        TtsMailFromOtk.findAll()
        SbMailFromCbr.findAll()
        ResponseToOrderCbr.findAll()

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

        // disable from 23.05.2018
        // ExecuteOverdraftJuric.findAll()
        //ExecutePosPot.findAll()

        CorrectPrim.findAll()
        ExecuteReglamentRun.findAll()

        ExecuteGroupRateLoan.findAll()
        // rita check
        //GeneralExecReserve283p.findAll()

        CorrDepartmentGoHome.findAll()

        SendBlockUnblockAccountPC.findAll()

        ClearPrimFromArchiveDay.findAll()

        ReportSender.findAll()

        MantisUserDisabled.findAll()

        executeTasks()
    }

}