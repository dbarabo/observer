package ru.barabo.observer.report

import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.xls.ExcelSimple
import java.io.File

object ReportXlsLockCards {

    fun createReport(): File {
        val file = File("${Cmd.tempFolder()}/${TEMPLATE_REPORT.name}")

        val excelSimple = ExcelSimple(file, TEMPLATE_REPORT).createTitle(emptyMap())

        excelSimple.createHeader(emptyMap())

        val rows = AfinaQuery.selectCursor(SELECT_LOCK_CARDS)

        val rowVar = createVarLoop()

        for (row in rows) {
            rowVar.putBodyRow(row)

            excelSimple.createBodyRow(rowVar)
        }

        excelSimple.save()

        return file
    }

    private fun MutableMap<String, Any>.putBodyRow(row: Array<Any?>) {

        val index = this["index"]  as Number
        this["index"] = index.toInt() + 1

        this["account"] = row[6].asString()

        this["customer"] = row[7].asString()

        this["person"] = row[8].asString()

        this["card"] = row[9].asString()

        this["status_lock"] = row[10].asString()

        this["who_lock"] = row[11].asString()

        this["date"] = row[12].asString()

        this["number_lock"] = row[13].asString()
    }

    private fun Any?.asString(): String = this?.toString() ?: ""

    private val TEMPLATE_REPORT = File("${Cmd.LIB_FOLDER}/lock_cards.xls")

    private const val SELECT_LOCK_CARDS = "{? = call od.PTKB_PLASTIC_REPORT.getLockCardList }"

    private fun createVarLoop(): MutableMap<String, Any> = mutableMapOf(
            "index" to 0,
            "account" to "",
            "customer" to "",
            "card" to "",
            "person" to "",
            "who_lock" to "",
            "date" to "",
            "number_lock" to "",
            "status_lock" to "")
}