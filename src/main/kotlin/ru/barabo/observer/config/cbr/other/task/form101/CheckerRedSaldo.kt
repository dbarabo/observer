package ru.barabo.observer.config.cbr.other.task.form101

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.cbr.other.task.form101.BalanceChecker101f.sendHtmlTable
import java.sql.Timestamp
import java.time.LocalDate

object CheckerRedSaldo {

    fun checkSaldo(dateReport: Timestamp) {

        val title = dateReport.toLocalDateTime().toLocalDate().title()

        val html = createHtmlData(dateReport, title)

        html?.let { sendHtmlTable(it, title) }
    }

    private fun createHtmlData(dateReport: Timestamp, title: String): String? {

        val data = AfinaQuery.selectCursor(CURSOR_CHECK_SALDO, arrayOf(dateReport))

        if(data.isEmpty()) return null

        val content = HtmlContent(title, title, HEADER_TABLE, data)

        return content.html()
    }

    private val HEADER_TABLE = mapOf(
            "Остаток номинал" to "right",
            "Остаток эквивалент" to "right",
            "Счет" to "left",
            "Наименование счета" to "left",
            "Тип счета" to "left",
            "id счета" to "right")

    private fun LocalDate.title() = "Красное сальдо на ${plusDays(1)}"

    private const val CURSOR_CHECK_SALDO = "{ ? = call od.PTKB_PRECEPT.checkRedSaldoAllAccounts( ? ) }"
}