package ru.barabo.observer.config.skad.anywork.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.clobToString
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ibank.task.LoanInfoSaver
import ru.barabo.observer.config.cbr.other.task.NbkiAllReportsSend
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
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

object RutdfCreateReport : Periodical {

    override var lastPeriod: LocalDateTime? = null

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override val accessibleData: AccessibleData =  AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(12, 0), LocalTime.of(16, 30), Duration.ofMinutes(1))

    override fun name(): String = "RUTDF создать файл"

    override fun config(): ConfigTask = AnyWork

    private fun xNbki() = "X:/НБКИ/".ifTest("C:/НБКИ/")

    private fun xNbkiToday() = "${xNbki()}${todayFolder()}"

    private fun todayFolder(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun execute(elem: Elem): State {
/*
        val (idFile, fileName) = getNewFile()

        val isExistsData = fillAndCheckExistsData(idFile)

        if(isExistsData) {*/

        val fileName = "K301BB000001_20220702_165040"
        val idFile = 1263855456L


        val textClob = AfinaQuery.execute(query = GET_DATA_FILE,
                params = arrayOf(idFile), outParamTypes = intArrayOf(OracleTypes.CLOB)) as Clob

            val folder = xNbkiToday().byFolderExists()

            val textFile = File("${folder.absolutePath}/$fileName.txt")

            textFile.writeText(textClob.clob2string(), charset = Charset.forName("CP1251"))
       // }

        return State.OK
    }

    private fun fillAndCheckExistsData(idFile: Number): Boolean {

        AfinaQuery.execute( FILL_DATA_RUTDF, arrayOf(idFile) )

        val isExists = selectValueType<Number>(IS_EXISTS_DATA, arrayOf(idFile))?.toInt() ?: 0

        return (isExists > 0)
    }

    private fun getNewFile(): Pair<Number, String> {
        val (id, fileName) = AfinaQuery.execute(query = CREATE_FILE,
            outParamTypes = intArrayOf(OracleTypes.NUMBER, OracleTypes.VARCHAR))!!

       return  Pair(id as Number, fileName as String)
    }

    private const val IS_EXISTS_DATA =
        "select count(*) from dual where exists (select 1 from od.ptkb_rutdf_main where RUTDF_FILE = ?)"

    private const val CREATE_FILE = "{ call od.PTKB_RUTDF.createRutdfFileOut(?, ?) }"

    private const val FILL_DATA_RUTDF = "{ call od.PTKB_RUTDF.prepareProcessEvents( ? ) }"

    private const val GET_DATA_FILE = "{ call od.PTKB_RUTDF.getAllDataFile(?, ?) }"
}