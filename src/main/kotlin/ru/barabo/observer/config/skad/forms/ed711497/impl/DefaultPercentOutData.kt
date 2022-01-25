package ru.barabo.observer.config.skad.forms.ed711497.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.skad.crypto.p311.validateXml
import ru.barabo.observer.config.skad.forms.ed711497.PercentOutData
import ru.barabo.observer.config.skad.plastic.task.saveXml
import ru.barabo.observer.config.task.ed711497.FLPROCRequest
import ru.barabo.observer.config.task.ed711497.InfoPercent
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class DefaultPercentOutData(dateStartReport: LocalDate) : PercentOutData {

    override val yearReport: String = dateStartReport.year.toString()

    override val codeFns: String = "2540"

    private val _data = createInfoPercents(
        java.sql.Date(Date.from(dateStartReport.withDayOfMonth(1)
        .atStartOfDay(ZoneId.systemDefault()).toInstant()).time) )

    override val infoPercents: List<InfoPercent>
        get() = _data

    fun createFile(): File {

        System.setProperty("jdk.xml.maxOccurLimit", "1000000")

        val file = File("${folderReportToday().absolutePath}/$fileNameFns")

        val xmlData = FLPROCRequest(this)

        saveXml(file, xmlData, "UTF-8", true)

        validateXml(file, xsd, ::errorFolder )

        return file
    }
}

private fun folderReportToday() = "$folderReport/${Get440pFiles.todayFolder()}".byFolderExists()

private fun errorFolder(): File = "${folderReportToday()}/ERROR".byFolderExists()

private val folderReport = "H:/Gu_cb/365-П".ifTest("C:/365-П")

private const val xsd =  "/xsd/fns-flproc-ru-root.xsd"

private const val fileNameFns = "fns-flproc-types.xml"

private fun createInfoPercents(dateYear: java.sql.Date): List<InfoPercent> {

    AfinaQuery.execute(EXEC_CHECK_CLIENTS_ORDER, arrayOf(dateYear) )

    val infoPercents = ArrayList<InfoPercent>()

    val info = AfinaQuery.selectCursor(SELECT_PERCENTS, arrayOf(dateYear))

    infoPercents.fillInfoPercent(info)

    val infoIp = AfinaQuery.selectCursor(SELECT_PERCENTS_IP, arrayOf(dateYear))

    infoPercents.fillInfoPercent(infoIp)

    return infoPercents
}

private fun MutableList<InfoPercent>.fillInfoPercent(data: List<Array<Any?>>) {
    val correctionNumber = "00"

    val propertyCorrection = "1"

    for(row in data) {
        this += InfoPercent(
            (row[15] as Number).toInt(),
            correctionNumber,
            propertyCorrection,
            (row[16] as Number).toInt(),
            row[14] as Date,
            row[7] as? String,
            row[17] as Number,
            row[3] as? Number ?: 0.0,
            row[12] as String,
            row[11] as String,
            row[13] as? String,
            row[8] as String,
            row[9] as String,
            row[10] as Date
        )
    }
}

private const val EXEC_CHECK_CLIENTS_ORDER = "{ call od.XLS_REPORT_ALL.initClientOrderIfExists( ? ) }"

private const val SELECT_PERCENTS = "{ ? = call od.XLS_REPORT_ALL.getClientPayPercentYear( ? ) }"

private const val SELECT_PERCENTS_IP = "{ ? = call od.XLS_REPORT_ALL.getClientPayYearIpOnly( ? ) }"

