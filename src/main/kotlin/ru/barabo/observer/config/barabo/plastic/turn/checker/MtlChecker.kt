package ru.barabo.observer.config.barabo.plastic.turn.checker

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.plastic.turn.checker.CtlChecker.sendHtmlTable

object MtlChecker {

    private val logger = LoggerFactory.getLogger(MtlChecker::class.java)

    fun check() {
        val ctlList = AfinaQuery.selectCursor(SELECT_MTL_LIST)

        for (row in ctlList) {

            val amount = (AfinaQuery.execute(query = EXEC_FILL_DATA, params = row,
                    outParamTypes = intArrayOf(OracleTypes.NUMBER))?.get(0) as? Number)?.toInt()?:0

            if(amount != 0) {
                logger.error("DIFF COUNT!!! ID_CTL = ${row[0]} DIFF = $amount")
            }

            val emptyList = AfinaQuery.selectCursor(SELECT_EMPTY)
            if(emptyList.isNotEmpty()) {
                sendEmptyList(emptyList, (row[0] as? Number)?.toLong() ?: 0)
            }

            val diffAmountsList = AfinaQuery.selectCursor(SELECT_DIFF_AMOUNT)
            if(diffAmountsList.isNotEmpty()) {
                senddiffAmountsList(diffAmountsList, (row[0] as? Number)?.toLong() ?: 0)
            }

            logger.error("processed end MTL = ${row[0]}")
        }
    }

    private fun senddiffAmountsList(diffAmountsList: List<Array<Any?>>, idMtl: Long) {

        val title = "Не сошлись суммы по транзакции idCtl = $idMtl"

        val content = HtmlContent(title, title, DIFF_HEADER, diffAmountsList)

        sendHtmlTable(content.html(), title)
    }

    private val DIFF_HEADER = mapOf(
            "id BBOKK" to "right",
            "id MIN Transact" to "right",
            "id Mtl" to "right",
            "debet" to "right",
            "credit" to "right",
            "DIFF debet" to "right",
            "NEW Transact debet" to "right",
            "BBOOK debet" to "right",

            "DIFF credit" to "right",
            "NEW Transact credit" to "right",
            "BBOOK credit" to "right")

    private fun sendEmptyList(emptyList: List<Array<Any?>>, idMtl: Long) {

        val title = "Не сопоставлены транзакции idCtl = $idMtl"

        val content = HtmlContent(title, title, EMPTY_HEADER, emptyList)

        sendHtmlTable(content.html(), title)
    }

    private val EMPTY_HEADER = mapOf(
            "id Transact" to "right",
            "id Mtl" to "right",
            "id Schema" to "right",
            "debet" to "right",
            "credit" to "right",
            "amountdebet" to "right",
            "amountcredit" to "right")

    private const val SELECT_MTL_LIST = "{ ? = call od.PTKB_PLASTIC_TURN.getTestMtlList }"

    private const val EXEC_FILL_DATA = "{ call od.PTKB_PLASTIC_TURN.testFillMtlData(?, ?) }"

    private const val SELECT_EMPTY = "{ ? = call od.PTKB_PLASTIC_TURN.getTestEmptyNewTransferMtl }"

    private const val SELECT_DIFF_AMOUNT = "{ ? = call od.PTKB_PLASTIC_TURN.getTestDiffAmountsMtl }"
}
