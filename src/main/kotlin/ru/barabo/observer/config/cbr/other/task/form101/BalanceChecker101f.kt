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
    fun check101form(idForm101: Number, dateReport: Timestamp, isSmashError: Boolean = false) {

        AfinaQuery.execute(EXEC_CHECK_BALANCE, arrayOf(idForm101))

        val title = dateReport.toLocalDateTime().toLocalDate().titleCorrect(idForm101)

        val html = createHtmlData(idForm101, title, isSmashError)

        html?.let { sendHtmlTable(it, title) }
    }

    private fun createHtmlData(idForm101: Number, title: String, isSmashError: Boolean): String? {

        val data = AfinaQuery.selectCursor(CURSOR_BALANCE_ERROR, arrayOf(idForm101))

        if(data.isEmpty()) return null

        if(isSmashError) setSmashError(data)

        val content = HtmlContent(title, title, HEADER_TABLE, data)

        return content.html()
    }

    private fun setSmashError(data: List<Array<Any?>>) {
        for (row in data) {

            val account = row[0] as? String

            val inDiff = (row[3] as Number).toInt()

            val outDiff = (row[4] as Number).toInt()

            val turnDebetDiff = (row[5] as Number).toInt()

            val turnCreditDiff = (row[6] as Number).toInt()

            if(isDefaultDiff(inDiff, outDiff, turnDebetDiff, turnCreditDiff) ||
               isDiff60323Rest2(account, inDiff, outDiff, turnDebetDiff, turnCreditDiff)){
                val params = arrayOf(row[row.lastIndex])

                AfinaQuery.execute(EXEC_SMASH_ERROR_PTKB, params)
            }
        }
    }

    private fun isDiff60323Rest2(account: String?, inDiff: Int, outDiff: Int, turnDebetDiff: Int, turnCreditDiff: Int): Boolean =
           (account  == "60323" &&//in listOf("60323", "30110", "30233") &&/
            inDiff <= 2 &&
            outDiff <= 2 &&
            turnDebetDiff <= 2 &&
            turnCreditDiff <= 2)

    private fun isDefaultDiff(inDiff: Int, outDiff: Int, turnDebetDiff: Int, turnCreditDiff: Int): Boolean =
            (inDiff <= 1 &&
            outDiff <= 1 &&
            turnDebetDiff <= 2 &&
            turnCreditDiff <= 2)

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

    private fun LocalDate.titleCorrect(id101Form: Number) =
            "Несоответствие баланса со сданной 101 формой на $this ptkb_ptkpsd_101form_detail.id = ${id101Form.toLong()}"

    private const val EXEC_CHECK_BALANCE = "{ call od.PTKB_PRECEPT.check101ByPtkbPsd101Id(?) }"

    private const val EXEC_SMASH_ERROR_PTKB = "update od.ptkb_ptkpsd_101form_detail set CHECKER = 1 where id = ?"

    private const val CURSOR_BALANCE_ERROR = "{ ? = call od.PTKB_PRECEPT.get101Diff(null, ? ) }"

    fun sendHtmlTable(htmlData: String, title: String) {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = title, body = htmlData, subtypeBody = "html")
    }
}