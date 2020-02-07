package ru.barabo.xls

import jxl.Workbook
import jxl.write.WritableCellFormat
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger(ExcelSimple::class.java)

fun createNewBook(newFile: File, templateFile: File): WritableWorkbook {

    var templateBook: Workbook? = null

    try {
        templateBook = Workbook.getWorkbook(templateFile)

        val newBook = Workbook.createWorkbook(newFile, templateBook)

        templateBook.close()

        return newBook
    } catch (e: Exception) {

        logger.error("createNewBook", e)

        templateBook?.close()

        throw Exception(e.message)
    }
}

fun WritableWorkbook.save() {
    write()

    close()
}

fun WritableSheet.addRow(rowIndex: Int) {

    this.insertRow(rowIndex)

    // новая строка уходит вверх - поэтому копируем с нижней(с большей)
    this.copyRow(rowIndex, rowIndex+1)
}

fun WritableSheet.copyRow(srcRowIndex: Int, destRowIndex: Int) {

    for (col in 1 until columns) {
        val readCell = getWritableCell(col, srcRowIndex)

        val newCell = readCell.copyTo(col, destRowIndex)

        readCell.cellFormat?.let {

            newCell.cellFormat = WritableCellFormat(it)
        }

        this.addCell(newCell)
    }
}

