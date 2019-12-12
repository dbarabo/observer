package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.cbr.other.task.nbki.ArraySheetData
import ru.barabo.observer.config.cbr.other.task.nbki.ExcelNbkiCreator
import ru.barabo.observer.config.cbr.other.task.nbki.SheetData
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.sql.Clob
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object NbkiAllReportsSend : Periodical {

    override var lastPeriod: LocalDateTime? = null

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override val accessibleData: AccessibleData =  AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(7, 0), LocalTime.of(16, 0), Duration.ofMinutes(1))

    override fun name(): String = "НБКИ отправить отчет"

    override fun config(): ConfigTask = OtherCbr

    private fun xNbki() = "X:/НБКИ/".ifTest("C:/НБКИ/")

    private fun xNbkiToday() = "${xNbki()}${todayFolder()}"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun execute(elem: Elem): State {

        fillData()

        val fileName = createNbkiTextFile() ?: return State.ARCHIVE

        val xlsFile = createNbkiXlsFile(fileName)

        sendMailReport(xlsFile)

        return State.OK
    }

    /*private*/ fun fillData() {
        AfinaQuery.execute(FILL_DATA_NBKI)
    }

    private const val FILL_DATA_NBKI = "{call od.PTKB_NBKI.fillAllData}"

    private const val SUBJECT_REPORT_XLS = "NBKI REPORT"

    private const val BODY_REPORT_XLS = "Внимание, файл для отправки в НБКИ готов и будет отправлен сегодня " +
            "в 16:00. Пожалуйста, проверьте корректность данных по вложенному отчету. Обратите внимание, " +
            "что отчет, книга Excel, содержит пять листов данных. На каждом листе могут быть отправляемые данные." +
            " Если захотите распечатать отчет, то установите масштаб страницы в 75% от натуральной величины."

    private fun sendMailReport(xlsFile :File) {

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.CREDIT, bcc = BaraboSmtp.AUTO, subject = SUBJECT_REPORT_XLS,
                body = BODY_REPORT_XLS, attachments = arrayOf(xlsFile))
    }

    /*private*/ fun createNbkiXlsFile(fileName :String) :File {

        val xlsFile = File("${xNbkiToday()}/$fileName.xls")

        val allSheetData = ArrayList<SheetData?>()

        (0..4).mapTo(allSheetData) { xlsSheetData(it) }

        ExcelNbkiCreator.create(xlsFile, allSheetData)

        return xlsFile
    }


    private const val SELECT_XLS_SHEET = "select OD.PTKB_NBKI.getExcelData(?, 0) from dual"

    private fun xlsSheetData(sheetOrder :Int) :SheetData? {

        val clob = AfinaQuery.selectValue(SELECT_XLS_SHEET, arrayOf(sheetOrder)) as Clob?

        return clob?.let { parseSheetClob(it.clob2string() ) }
    }

    private fun parseSheetClob(data: String): SheetData {

        val rows = data.split("\n")

        val sheetData = ArraySheetData()

        rows.forEach {
            sheetData.add( it.split("\b") )
        }

        return sheetData
    }

    fun createGuarantorTextFile(): String? {
        val fileName = AfinaQuery.selectValue(SELECT_TEXT_FILE) as String

        val textClob = AfinaQuery.selectValue(SELECT_TEXT_GURANTOR_ONLY) as Clob

        val folder = xNbkiToday().byFolderExists()

        val textFile = File("${folder.absolutePath}/$fileName.txt")

        textFile.writeText(textClob.clob2string(), Charset.forName("CP1251") )

        return fileName
    }

    /*private*/ fun createNbkiTextFile(): String? {

        val fileName = AfinaQuery.selectValue(SELECT_TEXT_FILE) as String

        val textClob = AfinaQuery.selectValue(SELECT_TEXT_DATA) as Clob

        val folder = xNbkiToday().byFolderExists()

        val textFile = File("${folder.absolutePath}/$fileName.txt")

        textFile.writeText(textClob.clob2string(), Charset.forName("CP1251") )

        if(textFile.length() <= 64) {
            textFile.delete()
            return null
        }
        return fileName
    }

    private const val SELECT_TEXT_GURANTOR_ONLY = "select OD.PTKB_NBKI.getOnlyTrGuarantorTUTDF600 from dual"

    private const val SELECT_TEXT_DATA = "select OD.PTKB_NBKI.getAllDataNoSend from dual"

    private const val SELECT_TEXT_FILE = "select OD.PTKB_NBKI.getFileName from dual"
}
