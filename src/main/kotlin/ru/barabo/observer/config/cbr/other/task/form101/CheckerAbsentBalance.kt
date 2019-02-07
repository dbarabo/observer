package ru.barabo.observer.config.cbr.other.task.form101

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.cbr.other.task.form101.BalanceChecker101f.sendHtmlTable

object CheckerAbsentBalance {

    fun checkAbsent() {

        val html = createHtmlData()

        html?.let { sendHtmlTable(it, TITLE) }
    }

    private fun createHtmlData(): String? {

        val data = AfinaQuery.selectCursor(CURSOR_CHECK_ABSENT)

        if(data.isEmpty()) return null

        val content = HtmlContent(TITLE, TITLE, HEADER_TABLE, data)

        return content.html()
    }

    private val HEADER_TABLE = mapOf(
            "id Проводки" to "left",
            "№ док-та" to "left",
            "Дата проводки" to "left",
            "Сумма по счету" to "right",
            "Сумма сконвертир." to "right",
            "Назначение платежа" to "left")

    private const val TITLE = "Отсутствие проводок в балансе для исполненных док-тов 5 категории и РПП"

    private const val CURSOR_CHECK_ABSENT = "{ ? = call od.PTKB_PRECEPT.checkExecDocAbsentBalance }"
}