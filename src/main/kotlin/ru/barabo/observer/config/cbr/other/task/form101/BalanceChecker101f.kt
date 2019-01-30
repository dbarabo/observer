package ru.barabo.observer.config.cbr.other.task.form101

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.sql.Timestamp
import java.time.LocalDate

object BalanceChecker101f {

    /**
     *  check sent 101 form by calculate rest & turn from real balance
     *  if exists problem then send mail with fail data
     */
    fun check101form(idForm101: Long, dateReport: Timestamp) {

        AfinaQuery.execute(EXEC_CHECK_BALANCE, arrayOf(idForm101))

        val title = dateReport.toLocalDateTime().toLocalDate().titleCorrect(idForm101)

        val html = createHtmlData(idForm101, title)

        html?.let { sendHtmlTable(it, title) }
    }

    private fun createHtmlData(idForm101: Long, title: String): String? {

        val data = AfinaQuery.selectCursor(CURSOR_BALANCE_ERROR, arrayOf(idForm101))

        if(data.isEmpty()) return null

        val content = HtmlContent(title, title, HEADER_TABLE, data)

        return content.html()
    }

    private val HEADER_TABLE = mapOf(
            "Счет" to "left",
            "Валюта" to "left",
            "Тип" to "left",
            "Diff in-остаток"  to "right",
            "Diff out-остаток"  to "right",
            "Diff дебет"  to "right",
            "Diff кредит"  to "right",
            "in-остаток Сдан"  to "right",
            "in-остаток Реал"  to "right",
            "out-остаток Сдан"  to "right",
            "out-остаток Реал"  to "right",
            "Дебет Сдан"  to "right",
            "Дебет Реал"  to "right",
            "Кредит Сдан"  to "right",
            "Кредит Реал"  to "right")

    private fun LocalDate.titleCorrect(id101Form: Long) =
            "Несоответствие баланса со сданной 101 формой на $this ptkb_ptkpsd_101form_detail.id = $id101Form"

    private const val EXEC_CHECK_BALANCE = "{ call od.PTKB_PRECEPT.check101ByPtkbPsd101Id(?) }"

    private const val CURSOR_BALANCE_ERROR = "{ ? = call od.PTKB_PRECEPT.get101Diff(null, ? ) }"

    private fun sendHtmlTable(htmlData: String, title: String) {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = title, body = htmlData, subtypeBody = "html")
    }
}