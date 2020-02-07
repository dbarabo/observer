package ru.barabo.xls

import jxl.write.*
import java.io.File
import java.util.regex.Pattern

class ExcelSimple(newFile: File, template: File) {

    private val newBook: WritableWorkbook = createNewBook(newFile, template)

    private var rowPosition: Int = 0

    private val templateData: List<RowXls>

    init {
        templateData = initData(newBook.getSheet(0))
    }

    fun save() = newBook.save()

    fun createTitle(titleVar: Map<String, Any>) = createRowType(titleVar, RowTypes.TITLE)

    fun createHeader(headerVar: Map<String, Any>) = createRowType(headerVar, RowTypes.HEADER)

    fun createTail(tailVar: Map<String, Any>) = createRowType(tailVar, RowTypes.TAIL)

    fun createBodyRow(bodyVar: Map<String, Any>) = createRowType(bodyVar, RowTypes.BODY)

    private fun initData(sheet: WritableSheet): List<RowXls> {

        val rowData = ArrayList<RowXls>()

        for (row in 0 until sheet.rows) {

            var rowXls :RowXls? = null

            val cell = sheet.getCell(0, row)

            val content = cell?.contents?.trim()?:""

            val rowType = RowTypes.typeByString(content)

            if(cell is Label) {
               cell.string = ""
            }

            for (col in 1 until sheet.columns) {

                val value = sheet.getCell(col, row)

                val contentValue = value?.contents

                val format = value.cellFormat?.let {WritableCellFormat(it)} ?: WritableCellFormat()

                val column = ColumnXls(col, format, contentValue?:"")

                rowXls = rowXls?.addColumn(column) ?: RowXls(rowType, row).addColumn(column)
            }

            rowXls?.let { rowData += it }
        }

        return rowData
    }

    private fun findMinIndexRowType(rowType: RowTypes): Int = templateData.filter { it.type === rowType }.map { it.index }.min()?:0

    private fun createRowType(variable: Map<String, Any>, rowType: RowTypes): ExcelSimple {
        val sheet = newBook.getSheet(0)

        var maxPosition = rowPosition

        val minType = findMinIndexRowType(rowType)

        val dataFilter = templateData.filter { it.type === rowType }

        if(dataFilter.isEmpty()) return this

        val isUseLast = dataFilter[0].lastUseRow

        dataFilter.forEach {

            val rowPos = rowPosition + it.index - minType

            if (isUseLast != null) {

                sheet.addRow(rowPos)

            } else {
                it.lastUseRow = rowPos
            }

            if (rowPos > maxPosition) maxPosition = rowPos

            it.columns.forEach {

                val parseValue = it.getVal(variable)

                val writeCell = when {
                    parseValue.toString().isEmpty() -> Blank(it.index, rowPos, it.format)
                    it.isNumberFormat(variable) -> jxl.write.Number(it.index, rowPos, toDouble(parseValue), it.format)
                    else -> Label(it.index, rowPos, parseValue.toString(), it.format)
                }

                sheet.addCell(writeCell)
            }
        }

        rowPosition = maxPosition + 1

        return this
    }

    private fun toDouble(value: Any): Double {

        return if(value is Number) value.toDouble() else try {

            value.toString().toDouble()

        } catch (e: Exception) {
            0.0
        }
    }
}

data class RowXls(val type: RowTypes,
                  val index: Int,
                  val columns: MutableList<ColumnXls> = ArrayList()) {

    var lastUseRow: Int? = null

    fun addColumn(column: ColumnXls): RowXls {
        columns += column

        return this
    }
}

data class ColumnXls(val index: Int,
                     val format: WritableCellFormat,
                     val value: String) {

    private fun isSimpleVar(): Boolean = value.indexOf('[') == 0 && value.indexOf(']') == value.length - 1

    private fun simpleValue(titleVar: Map<String, Any>): Any {

        val variable = value.substring(1, value.length - 1).trim()

        return titleVar[variable]?:""
    }

    fun isNumberFormat(titleVar: Map<String, Any>): Boolean {

      if(!isSimpleVar()) return false

       return (simpleValue(titleVar) is Number) //&& format.isNumber
    }

    fun getVal(titleVar: Map<String, Any>): Any {

        return if(isSimpleVar()) {
            simpleValue(titleVar)
        } else {
            complexValue(titleVar)
        }
    }

    private fun complexValue(titleVar: Map<String, Any>): Any {
        var newValue = value

        val matcher = Pattern.compile("\\[(.*?)\\]").matcher(newValue)


        while (matcher.find()) {

            val varName = matcher.group(1)

            val varValue = titleVar[varName]?.toString() ?: varName

            newValue = newValue.replace("\\[$varName\\]".toRegex(), varValue)
        }

        return newValue
    }
}

enum class RowTypes {

    TITLE,
    HEADER,
    BODY,
    TAIL;

    companion object {

        fun typeByString(nameType: String): RowTypes = values().firstOrNull { it.name == nameType} ?: TITLE
    }
}
