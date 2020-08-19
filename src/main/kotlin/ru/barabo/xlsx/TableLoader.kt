package ru.barabo.xlsx

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import java.io.File
import java.io.FileInputStream

abstract class TableLoader {

    private var stateRow: TypeLine = TypeLine.NOTHING

    protected fun loadXlsx(sourceXlsx: File) {
        FileInputStream(sourceXlsx).use { stream ->
            XSSFWorkbook(stream).use { workbook ->
                return load(workbook.getSheetAt(0) )
            }
        }
    }

    private fun load(sheet: XSSFSheet) {
        val rows = sheet.iterator()

        while (rows.hasNext()) {
            val cellsRow = processRow(rows.next() )

            if(cellsRow.isEmpty()) continue

            executeRow(cellsRow)
        }
    }

    private fun executeRow(cellsRow: List<Cell>) {
        when(stateRow) {
            TypeLine.HEADER -> executeHeader(cellsRow)

            TypeLine.BODY -> executeBody(cellsRow)

            TypeLine.TAIL -> executeTail(cellsRow)

            TypeLine.NOTHING -> {}
        }
    }

    private fun processRow(row: Row): List<Cell> {

        val cells = row.cellIterator()

        val columnsXlsx = ArrayList<Cell>()

        while (cells.hasNext()) {
            val cell = cells.next()

            columnsXlsx += cell
        }

        return processRowData(columnsXlsx)
    }

    private fun processRowData(columns: MutableList<Cell>): List<Cell> {
        if(isSkipRow(columns, stateRow) ) return emptyList()

        stateRow = processStateRow(columns, stateRow)

        return columns
    }

    protected abstract fun executeTail(columns: List<Cell>)

    protected abstract fun executeBody(columns: List<Cell>)

    protected abstract fun executeHeader(columns: List<Cell>)

    protected abstract fun isSkipRow(columns: List<Cell>, stateRow: TypeLine): Boolean

    protected abstract fun processStateRow(columns: MutableList<Cell>, stateRow: TypeLine): TypeLine
}