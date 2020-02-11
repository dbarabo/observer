package ru.barabo.xls

import jxl.write.WritableCellFormat
import jxl.write.WritableWorkbook
import java.io.File

class ExcelSql(newFile: File, template: File) {

    private val newBook: WritableWorkbook = createNewBook(newFile, template)


}

data class Row(val tag: Tag,
               val index: Int,
               val columns: MutableList<ColumnXls> = ArrayList())

data class Col(val index: Int,
               val format: WritableCellFormat,
               val value: String)

enum class Tag {
    EMPTY,
    LOOP,
    IF
}