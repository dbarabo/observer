package ru.barabo.observer.config.cbr.other.task

import ru.barabo.cmd.deleteFolder
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.report.ReportXlsLockCards
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File

object ReportSender : SingleSelector {

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS)

    override fun name(): String = "Отчетики"

    override fun config(): ConfigTask = OtherCbr

    override val select: String = "select id, REPORT_NAME from od.ptkb_wait_report where state = 0 and DELIVERY_DATE < sysdate"

    override fun execute(elem: Elem): State {

        val idWaitReport = elem.idElem ?: return State.OK

        val file = mapReports[elem.name]?.invoke() ?: return okReport(idWaitReport)

        val mailTo = selectValueType<String>(SELECT_REPORT, arrayOf(idWaitReport)) ?: return okReport(idWaitReport)

        return sendReport(file, mailTo, idWaitReport)
    }

    private fun sendReport(file: File, mailTo: String, idWaitReport: Long): State {

        BaraboSmtp.sendStubThrows(to = arrayOf(mailTo), subject = "Доставка отчета", body = "", attachments = arrayOf(file) )

        file.parentFile.deleteFolder()

        return okReport(idWaitReport)
    }

    private fun okReport(idWaitReport: Long): State {
        AfinaQuery.execute(UPDATE_STATE_OK, arrayOf(idWaitReport) )

        return State.OK
    }

    private const val UPDATE_STATE_OK = "update od.ptkb_wait_report set state = 1 where id = ?"

    private const val SELECT_REPORT = "select EMAIL from od.ptkb_wait_report where id = ?"

    private val mapReports = mapOf<String, ()-> File>(
            "LOCKED_CARD_LIST" to ReportXlsLockCards::createReport
    )
}