package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.skad.crypto.p311.validateXml
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.skad.forms.form310.ExecutorForm
import ru.barabo.observer.config.skad.forms.form310.Form310Data
import ru.barabo.observer.config.skad.plastic.task.saveXml
import ru.barabo.observer.config.task.form310.Form310Xml
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class DefaultForm310Data(dateStartReport: LocalDate) : Form310Data {

    override val dateReport: Date = Date.from(dateStartReport.withDayOfMonth(1)
        .atStartOfDay(ZoneId.systemDefault()).toInstant())

    private val _data = checkCreateData310Form(dateReport)

    override val leader: ExecutorForm = leaderPtkb

    override val chiefAccountant: ExecutorForm = chiefAccountantPtkb

    override val executor: ExecutorForm = executorPtkb

    override val data: Data310Form?
        get() = _data

    fun createFile(): File {

        val file = File("${folderReportToday().absolutePath}/$fileName310")

        val xmlData = Form310Xml(this)

        saveXml(file, xmlData, "windows-1251", true)

        validateXml(file, xsd, ::errorFolder )

        return file
    }
}

private fun folderReportToday() = "$folderReport/${Get440pFiles.todayFolder()}".byFolderExists()

private fun errorFolder(): File = "${folderReportToday()}/ERROR".byFolderExists()

private val leaderPtkb = ExecutorForm("Председатель Правления", "Сима Оксана Анатольевна")

private val chiefAccountantPtkb = ExecutorForm("Врио главного бухгалтера", "Паллас Светлана Александровна")

private val executorPtkb = ExecutorForm("Ведущий экономист", "Кудрявцева Людмила Федоровна", "226-98-31")

private val folderReport = "H:/Dep_Buh/310".ifTest("C:/Dep_Buh/310")

private val xsd =  "/xsd/Ф310_Schema.xsd"

private const val fileName310 = "OES.xml"//"Ф310_40507717.xml"