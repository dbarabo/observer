package ru.barabo.xlsx

import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFSheet
import java.io.File
import java.io.FileInputStream

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.lang.Exception

class SheetReader(private val sourceXlsx: File) {

    fun readSheet(): List<RowXlsx> {
        FileInputStream(sourceXlsx).use { stream ->
            XSSFWorkbook(stream).use { workbook ->
                return readSheetData(workbook.getSheetAt(0) )
            }
        }
    }

    private fun readSheetData(sheet: XSSFSheet): List<RowXlsx> {
        val rows = sheet.iterator()

        val result = ArrayList<RowXlsx>()

        while (rows.hasNext()) {
            val row = rows.next()

            val columnsXlsx = ArrayList<Cell>()

            val rowXlsx = RowXlsx(row.rowNum, columnsXlsx)

            val cells = row.cellIterator()

            while (cells.hasNext()) {
                val cell = cells.next()

                columnsXlsx += cell
            }

            result += rowXlsx
        }

        return result
    }
}

fun Cell.byString(isConvertNumericToDate: Boolean = true): String {

    return when (cellType) {
        CellType.NUMERIC -> if(isConvertNumericToDate) {
            this.localDateTimeCellValue.toString()
        } else {
            this.numericCellValue.toString()
        }
        CellType.STRING -> this.stringCellValue
        CellType.BLANK -> ""
        CellType.BOOLEAN -> this.booleanCellValue.toString()
        CellType.FORMULA -> this.cellFormula
        CellType.ERROR -> this.errorCellValue.toString()
        else -> throw Exception("UNKNOWN TYPE $cellType")
    }
}

data class RowXlsx(val rowIndex: Int, val columns: List<Cell>)


