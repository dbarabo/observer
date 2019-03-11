package ru.barabo.observer.config.cbr.other.task.form101

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.cbr.other.task.form101.BalanceChecker101f.sendHtmlTable

object CheckerDoubleTurn {

    fun checkDouble() {

        val html = createHtmlData()

        html?.let { sendHtmlTable(it, TITLE) }
    }

    private fun createHtmlData(): String? {

        val data = AfinaQuery.selectCursor(CURSOR_CHECK_DOUBLE)

        if(data.isEmpty()) return null

        val content = HtmlContent(TITLE, TITLE, HEADER_TABLE, data)

        return content.html()
    }

    private val HEADER_TABLE = mapOf(
            "id Проводки" to "left",
            "id Дебета" to "left",
            "id Кредита" to "left",
            "Сумма" to "right",
            "Кол-во дублей" to "right",
            "min operdate" to "right",
            "max operdate" to "right",
            "№ док-та" to "left")

    private const val TITLE = "Задвоение проводок в балансе"

    private const val CURSOR_CHECK_DOUBLE = "{ ? = call od.PTKB_PRECEPT.checkDoubleBBook }"
}
