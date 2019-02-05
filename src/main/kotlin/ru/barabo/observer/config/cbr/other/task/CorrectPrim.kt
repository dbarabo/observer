package ru.barabo.observer.config.cbr.other.task

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.cbr.other.task.form101.BalanceChecker101f
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDate

object CorrectPrim : SingleSelector {

    override val select: String =
            "select id, to_char(date_report, 'dd.mm.yyyy') from od.ptkb_ptkpsd_101form where state = 0 " +
                    "and upper(TYPE_REPORT) = 'НЕРЕГУЛЯРНАЯ'"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS)

    override fun name(): String = "Правка показателей ежеднев."

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        val reportDate = AfinaQuery.selectValue(SELECT_DATE, arrayOf(elem.idElem))

        AfinaQuery.execute(EXEC_CORRECT_PRIM, arrayOf(reportDate))

        sendReportCorrect(elem.idElem, reportDate as Timestamp)

        elem.idElem?.let{ BalanceChecker101f.check101form( it, reportDate) }

        return State.OK
    }

    private const val SELECT_DATE = "select date_report from od.ptkb_ptkpsd_101form where id = ?"

    private const val EXEC_CORRECT_PRIM = "call od.PTKB_PRECEPT.correctPrimBalance(?)"

    private fun sendReportCorrect(idElem: Long?, dateReport: Timestamp) {

        val html = createHtmlData(idElem, dateReport.toLocalDateTime().toLocalDate())

        html?.let { sendHtmlTable(it, dateReport.toLocalDateTime().toLocalDate()) }
                ?: sendTextOnlyEmpty(dateReport.toLocalDateTime().toLocalDate())
    }

    private fun sendHtmlTable(htmlData: String, date: LocalDate) {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.BOOKER, cc = BaraboSmtp.PRIM_AUTO, subject = date.titleCorrect(),
                body = htmlData, subtypeBody = "html")
    }

    private fun sendTextOnlyEmpty(date: LocalDate) {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.BOOKER, cc = BaraboSmtp.PRIM_AUTO, subject = date.titleEmptyCorrect(),
                body = date.titleEmptyCorrect())
    }

    private fun createHtmlData(idElem: Long?, dateReport: LocalDate): String? {

        val data = AfinaQuery.selectCursor(CURSOR_REPORT_CORRECT, arrayOf(idElem))

        if(data.isEmpty()) return null

        val content = HtmlContent(dateReport.titleCorrect(), dateReport.titleCorrect(), HEADER_TABLE, data)

        return content.html()
    }

    private fun LocalDate.titleCorrect() = "Измененные показатели на $this (за ${this.minusDays(1)})"

    private fun LocalDate.titleEmptyCorrect() = "Нет изменений показателей на $this (за ${this.minusDays(1)})"

    private val HEADER_TABLE = mapOf(
            "Счет" to "left",
            "Валюта" to "left",
            "Новый остаток"  to "right",
            "Старый остаток"  to "right",
            "Новый дебет"  to "right",
            "Старый дебет"  to "right",
            "Новый кредит"  to "right",
            "Старый кредит" to "right")

    private const val CURSOR_REPORT_CORRECT = "{ ? = call od.PTKB_PRECEPT.getCorrectPrimView( ? ) }"
}
