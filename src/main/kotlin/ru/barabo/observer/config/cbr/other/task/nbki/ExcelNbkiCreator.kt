package ru.barabo.observer.config.cbr.other.task.nbki

import jxl.Workbook
import jxl.format.*
import jxl.format.Alignment
import jxl.format.Border
import jxl.format.BorderLineStyle
import jxl.format.Colour
import jxl.write.*
import jxl.write.WritableFont.ARIAL
import jxl.write.biff.RowsExceededException
import ru.barabo.cmd.Cmd
import java.io.File
import java.sql.Clob
import java.time.LocalDate
import java.time.format.DateTimeFormatter

typealias RowData = List<String?>

typealias SheetData = List<RowData?>

typealias ArraySheetData = ArrayList<RowData>

fun Clob.clob2string() :String = this.getSubString(1, this.length().toInt())

object ExcelNbkiCreator {

    private fun templateNbki() = File("${Cmd.LIB_FOLDER}/nbki403.xls")

    fun create(xlsFile : File, sheetDataList :List<SheetData?>) {

        val workbook = Workbook.getWorkbook(templateNbki())

        val copy = Workbook.createWorkbook(xlsFile, workbook)

        sheetDataList.indices.forEach {
            exportTotal(copy.getSheet(it), sheetDataList[it])
        }

        workbook.close()

        copy.write()
        copy.close()
    }

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    @Throws(RowsExceededException::class, WriteException::class)
    private fun showHeader(sheet: WritableSheet) {

        val cell = sheet.getCell(0, 0)

        var cellFrmt = cell.cellFormat
        if (cellFrmt == null) {
            cellFrmt = getDefaultCellFormat()
        }

        val lab = Label(0, 0, "${cell.contents} ${todayFolder()}", cellFrmt)

        sheet.addCell(lab)
    }

    private fun getDefaultCellFormat(): WritableCellFormat {
        //установка шрифта
        val arial12ptBold = WritableFont(ARIAL, 10/*, WritableFont.NO_BOLD*/)
        var arial12BoldFormat = WritableCellFormat(arial12ptBold)

        try {
            arial12BoldFormat = WritableCellFormat(arial12ptBold)
            arial12BoldFormat.alignment = Alignment.LEFT //выравнивание по центру
            arial12BoldFormat.wrap = true //перенос по словам если не помещается
            arial12BoldFormat.setBackground(Colour.WHITE) //установить цвет
            arial12BoldFormat.setBorder(Border.ALL, BorderLineStyle.THIN) //рисуем рамку

        } catch (e: WriteException) {}

        return arial12BoldFormat
    }

    private fun exportTotal(sheet : WritableSheet, data :SheetData?, marker :Marker? = null) {

        if (data == null || data.isEmpty() || data[0] == null) return

        val colCount = data[0]?.size?:-1

        val initRowXls = 3

        var row = initRowXls

        showHeader(sheet)

        for (rowData in data) {

            if (rowData?.size?:-1 < colCount) break

            for (col in 0 until colCount) {

                val cell = sheet.getCell(col, initRowXls)

                var cellFrmt = cell.cellFormat
                if (cellFrmt == null) {
                    cellFrmt = getDefaultCellFormat()
                }

                val fmt = WritableCellFormat(cellFrmt)
                fmt.setBackground(Colour.WHITE)
                cellFrmt = fmt

                marker?.mark(col, rowData?.get(col)?:"", cellFrmt)

                val lab = Label(col, row, rowData?.get(col)?:"", cellFrmt)

                sheet.addCell(lab)
            }
            row++
        }
    }
}

internal class Marker(private val isEqual: BooleanArray,
             private val data: Array<Array<String>>,
             private val checkCols: IntArray?) {

    @Throws(WriteException::class)
    fun mark(col: Int, cell: String, cellFrmt: CellFormat) {

        if (checkCols == null) return

        for (index in checkCols.indices) {

            markCell(col,
                    checkCols[index],
                    cell, cellFrmt,
                    isEqual[index],
                    data[index])
        }
    }

    @Throws(WriteException::class)
    private fun markCell(col: Int, checkCol: Int, cell: String?,
                         cellFrmt: CellFormat, isEqual: Boolean, data: Array<String>) {

        if (col != checkCol || cell == null || "" == cell) return

        var isCheck = data.contains(cell)

        if (!isEqual) {
            isCheck = !isCheck
        }

        if (isCheck) {
            (cellFrmt as WritableCellFormat).setBackground(Colour.YELLOW)
        }
    }
}