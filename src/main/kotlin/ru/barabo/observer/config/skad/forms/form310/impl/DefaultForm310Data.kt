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

fun folderReportToday() = "$folderReport/${Get440pFiles.todayFolder()}".byFolderExists()

private fun errorFolder(): File = "${folderReportToday()}/ERROR".byFolderExists()

private val leaderPtkb = ExecutorForm("Председатель Правления", "Сима Оксана Анатольевна")

private val chiefAccountantPtkb = ExecutorForm("Главный бухгалтер", "Паллас Светлана Александровна")

private val executorPtkb = ExecutorForm("заместитель начальника Кредитного департамента", "Василенко Наталья Валентиновна", "423-222-30-84")

private val folderReport = "H:/Dep_Kred/ОТЧЕТЫ КД/310_ФОРМА".ifTest("C:/Dep_Kred/ОТЧЕТЫ КД/310_ФОРМА")

private val xsd =  "/xsd/Ф310_Schema.xsd"

private const val fileName310 = "OES.xml"