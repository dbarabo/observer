package ru.barabo.observer.config.cbr.ibank.task

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.finder.isFind
import java.io.File
import java.sql.Timestamp
import java.util.regex.Pattern

private val logger = LoggerFactory.getLogger(CheckerXFile::class.java)

object CheckerXFile {

    private lateinit var newBook: Workbook

    private lateinit var sheet: Sheet

    private lateinit var fileName: String

    private lateinit var dateFile: Timestamp

    private val pathOt = File(PATH_OT)

    private val patternOt = Pattern.compile(REGEXP_OT, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

    private val findFiles: MutableList<String> = ArrayList()

    fun findProcess() {

        try {
            processNoError()
        } catch (e : Exception) {
            logger.error("CheckerXFile", e)
        }
    }

    private fun processNoError() {
        val newFiles = pathOt.listFiles { f ->
            (!f.isDirectory) &&
                    (patternOt.isFind(f.name)) &&
                    (!findFiles.contains(f.name.toUpperCase()))
        }

        if(newFiles.isNullOrEmpty()) return

        for(newFile in newFiles) {
            val isExists = (AfinaQuery.selectValue(SELECT_CHECK_FILE, arrayOf(newFile.name.toUpperCase())) as Number).toInt()

            if(isExists == 0) {
                addData(newFile)
            }

            findFiles.add( newFile.name.toUpperCase() )
        }
    }

    private fun addData(file: File) {

        try {
            fileName = file.name.toUpperCase()

            dateFile = fileName.substring(6..13).toDate().toTimestamp()

            newBook = createNewBook(file)

            sheet = newBook.getSheetAt(0)

            val rows = sheet.iterator()

            readRows(rows)
        } finally {
            newBook.close()
        }
    }

    private fun readRows(rows: MutableIterator<Row>) {
        while (rows.hasNext()) {
            val rowXls: Row = rows.next()
            if(rowXls.rowNum == 0) continue

            val lastName = rowXls.getCell(1)?.takeIf { it.cellType == CellType.STRING }?.stringCellValue?.trim()?.toUpperCase() ?: return

            if(lastName.isEmpty()) return

            val firstName = rowXls.getCell(2).stringCellValue.trim().toUpperCase()

            val secondName = rowXls.getCell(3).stringCellValue.trim().toUpperCase()

            val codeId = rowXls.getCell(4).stringCellValue.trim()

            val amount = (rowXls.getCell(5).numericCellValue * 100).toLong()

            val amountHold = parseNumberSeparator(rowXls.getCell(7).stringCellValue)

            val fio = "$lastName $firstName $secondName".trim()

            val params: Array<Any?> = arrayOf(codeId, amount, fio, amountHold, fileName, dateFile)

            AfinaQuery.execute(INSERT_CLIENT_HOLD, params)
        }
    }
}

private fun createNewBook(templateFile: File): Workbook {

    try {
        return XSSFWorkbook(templateFile)

    } catch (e: Exception) {

        logger.error("createNewBook", e)

        throw Exception(e.message)
    }
}

private const val REGEXP_OT = "ZPR_\\d_\\d\\d\\d\\d\\d\\d\\d\\d_2540015598\\.xls"

private const val PATH_OT = "H:/Dep_Buh/Зарплатный проект Открытие/Исходящие файлы/Зарплата"

private const val INSERT_CLIENT_HOLD =
    "insert into od.ptkb_zil_client (ID, CODE_ID, AMOUNT, CLIENT, AMOUNT_HOLD, FILE_NAME, CREATED) values (classified.nextval, ?, ?, ?, ?, ?, ?)"