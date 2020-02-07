package ru.barabo.xls

import jxl.write.WritableWorkbook
import java.io.File

class ExcelSql(newFile: File, template: File) {

    private val newBook: WritableWorkbook = createNewBook(newFile, template)


}