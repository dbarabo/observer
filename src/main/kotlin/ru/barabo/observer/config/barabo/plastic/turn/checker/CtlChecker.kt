package ru.barabo.observer.config.barabo.plastic.turn.checker

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import java.io.File
import java.nio.charset.Charset

object CtlChecker {

    private val logger = LoggerFactory.getLogger(CtlChecker::class.java)

    fun check() {
        val ctlList = AfinaQuery.selectCursor(SELECT_CTL_LIST)

        for (row in ctlList) {

            val amount = (AfinaQuery.execute(query = EXEC_FILL_DATA, params = row,
                    outParamTypes = intArrayOf(OracleTypes.NUMBER))?.get(0) as? Number)?.toInt()?:0

            if(amount != 0) {
                logger.error("DIFF COUNT!!! ID_CTL = ${row[0]} DIFF = $amount")
            }

            val emptyList = AfinaQuery.selectCursor(SELECT_EMPTY)
            if(emptyList.isNotEmpty()) {
                sendEmptyList(emptyList, (row[0] as? Number)?.toLong()?:0)
            }

            val diffAmountsList = AfinaQuery.selectCursor(SELECT_DIFF_AMOUNT)
            if(diffAmountsList.isNotEmpty()) {
                senddiffAmountsList(diffAmountsList, (row[0] as? Number)?.toLong()?:0 )
            }

            logger.error("processed end ctl = ${row[0]}")
        }
    }

    private const val SELECT_CTL_LIST = "{ ? = call od.PTKB_PLASTIC_TURN.getTestCtlList }"

    private const val EXEC_FILL_DATA = "{ call od.PTKB_PLASTIC_TURN.testFillCtlData(?, ?) }"

    private const val SELECT_EMPTY = "{ ? = call od.PTKB_PLASTIC_TURN.getTestEmptyNewTransfer }"

    private const val SELECT_DIFF_AMOUNT = "{ ? = call od.PTKB_PLASTIC_TURN.getTestDiffAmounts }"

    private fun senddiffAmountsList(diffAmountsList: List<Array<Any?>>, idCtl: Long) {

        val title = "Не сошлись суммы по транзакции idCtl = $idCtl"

        val content = HtmlContent(title, title, DIFF_AMOUNT_HEADER, diffAmountsList)

        sendHtmlTable(content.html(), title)
    }

    private val DIFF_AMOUNT_HEADER = mapOf(
            "id Bbook" to "right",
            "id Transact" to "right",
            "Терминал" to "left",
            "TransType"  to "left",
            "Diff DEBET"  to "right",
            "дебет New Trans"  to "left",
            "дебет BBOOK"  to "left",
            "Diff CREDIT"  to "right",
            "кредит New Trans"  to "left",
            "кредит BBOOK"  to "left",

            "Diff SUM debet" to "right",
            "Diff SUM credit" to "right",
            "New Trans debet" to "right",
            "BBOOK debet" to "right",

            "New Trans credit" to "right",
            "BBOOK credit" to "right"
            )

    private fun sendEmptyList(emptyList: List<Array<Any?>>, idCtl: Long) {

        val title = "Не сопоставлены транзакции idCtl = $idCtl"

        val content = HtmlContent(title, title, EMPTY_HEADER, emptyList)

        sendHtmlTable(content.html(), title)
    }

    private val EMPTY_HEADER = mapOf(
            "id Bbook" to "right",
            "id Transact" to "right",
            "Терминал" to "left",
            "TransType"  to "left",
            "Diff DEBET"  to "right",
            "дебет New Trans"  to "left",
            "дебет BBOOK"  to "left",
            "Diff CREDIT"  to "right",
            "кредит New Trans"  to "left",
            "кредит BBOOK"  to "left")

    internal fun sendHtmlTable(htmlData: String, title: String) {

        val file = File("C:/Картстандарт/test/$title.html")

        file.writeText(htmlData, Charset.forName("windows-1251"))

        //BaraboSmtp.sendStubThrows(to = BaraboSmtp.YA, subject = title, body = htmlData, subtypeBody = "html")
    }
}