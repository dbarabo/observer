package ru.barabo.observer.config.skad.anywork.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
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
        LocalTime.of(10, 0), LocalTime.of(20, 0),
        Duration.ofMinutes(119))

    override fun name(): String = "RUTDF создать файл"

    override fun config(): ConfigTask = AnyWork

    private fun xNbkiToday() = "${xNbki()}${todayFolder()}"

    private fun todayFolder(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun execute(elem: Elem): State {

        val sessionUni = AfinaQuery.uniqueSession()

        try {
            val (idFile, fileName) = getNewFile(sessionUni)

            val isExistsData = fillAndCheckExistsData(idFile, sessionUni)

            if (isExistsData) {

                val textClob = AfinaQuery.execute(
                    query = GET_DATA_FILE,
                    params = arrayOf(idFile),
                    sessionSetting = sessionUni,
                    outParamTypes = intArrayOf(OracleTypes.CLOB)
                )!![0] as Clob

                val folder = xNbkiToday().byFolderExists()

                val textFile = File("${folder.absolutePath}/$fileName.txt")

                textFile.writeText(textClob.clob2string(), charset = Charset.forName("CP1251"))

                AfinaQuery.commitFree(sessionUni)

            } else {
                AfinaQuery.rollbackFree(sessionUni)
            }

        } catch (e: Exception) {

            LoggerFactory.getLogger(RutdfCreateReport::class.java).error("exec", e)

            AfinaQuery.rollbackFree(sessionUni)

            throw Exception(e.message)
        }
        return State.OK
    }

    private fun fillAndCheckExistsData(idFile: Number, sessionUni: SessionSetting = SessionSetting(false) ): Boolean {

        AfinaQuery.execute( FILL_DATA_RUTDF, arrayOf(idFile), sessionUni )

        val isExists = selectValueType<Number>(IS_EXISTS_DATA, arrayOf(idFile), sessionUni)?.toInt() ?: 0

        return (isExists > 0)
    }

    private fun getNewFile(sessionUni: SessionSetting = SessionSetting(false)): Pair<Number, String> {
        val (id, fileName) = AfinaQuery.execute(query = CREATE_FILE,
            outParamTypes = intArrayOf(OracleTypes.NUMBER, OracleTypes.VARCHAR),
            sessionSetting = sessionUni
        )!!

       return  Pair(id as Number, fileName as String)
    }

    private const val IS_EXISTS_DATA =
        "select count(*) from dual where exists (select 1 from od.ptkb_rutdf_main where RUTDF_FILE = ?)"

    private const val CREATE_FILE = "{ call od.PTKB_RUTDF.createRutdfFileOut(?, ?) }"

    private const val FILL_DATA_RUTDF = "{ call od.PTKB_RUTDF.prepareProcessEvents( ? ) }"

    private const val GET_DATA_FILE = "{ call od.PTKB_RUTDF.getAllDataFile(?, ?) }"
}

fun xNbki() = "X:/НБКИ/".ifTest("C:/НБКИ/")