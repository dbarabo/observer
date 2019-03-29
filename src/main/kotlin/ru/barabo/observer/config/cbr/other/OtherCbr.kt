package ru.barabo.observer.config.cbr.other

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.cbr.ptkpsd.task.SendBlockUnblockAccountPC
import java.util.*

object OtherCbr: ConfigTask {

    override var timer: Timer? = null

    val logger = LoggerFactory.getLogger(OtherCbr::class.java)!!

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

        LoanInfoCreator.findAll()
        LoanInfoSaver.findAll()

        SendBlockUnblockAccountPC.findAll()

        ReportSender.findAll()

        executeTasks()
    }

}