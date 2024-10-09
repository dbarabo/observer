package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.impl.GutdfDataFromRutdf
import ru.barabo.observer.config.skad.crypto.p311.validateXml
import ru.barabo.observer.config.skad.plastic.task.saveXml
import ru.barabo.observer.config.task.nbki.gutdf.MainDocument
import java.io.File
import java.time.LocalDate

object GutDfCreator {

    private val logger = LoggerFactory.getLogger(GutDfCreator::class.java)

    fun createPullTest(from: LocalDate, to: LocalDate) {

        val list = AfinaQuery.select(TEST_SEL_RUTDF_LIST, params = arrayOf(from.toTimestamp(), to.toTimestamp()))

        for(element in list) {
            logger.error("file=${element[1]}")

            createFileByRutdf((element[0] as Number).toLong())
        }
    }

    fun createFileByRutdf(idRutdf: Long?): File {

        var newIdRutdf: Long = 0
       // try {
            newIdRutdf = idRutdf!!// ?: generateNewRutdf()

            val gutdfDataFromRutdf = GutdfDataFromRutdf(newIdRutdf)

            val file = File("${folderGutDfToday().absolutePath}/${gutdfDataFromRutdf.filenameWithoutExt()}.xml")

            val xmlData = MainDocument(gutdfDataFromRutdf)

            saveXml(file, xmlData, "UTF-8", true)

            validateXml(file, xsd, ::errorFolder )

            return file

//        } catch (e: Exception) {
//
//            logger.error("createFileByRutdf idRutdf=$idRutdf newIdRutdf=$newIdRutdf", e)
//
//            if(idRutdf == null) {
//                AfinaQuery.execute(DEL_RUTDF_DATA, params = arrayOf(newIdRutdf))
//            }
//
//            throw Exception(e)
//        }
    }

    private fun generateNewRutdf(): Long {
        val sessionUni = AfinaQuery.uniqueSession()

        try {
            val (idFile, _) = getNewFile(sessionUni)

            val isExistsData = fillAndCheckExistsData(idFile, sessionUni)

            if(isExistsData) {
                AfinaQuery.commitFree(sessionUni)
            } else {
                AfinaQuery.rollbackFree(sessionUni)
            }

            return idFile.toLong()

        } catch (e: Exception) {

            logger.error("generateNewRutdf", e)

            AfinaQuery.rollbackFree(sessionUni)

            throw Exception(e)
        }
    }

    private fun getNewFile(sessionUni: SessionSetting = SessionSetting(false)): Pair<Number, String> {
        val (id, fileName) = AfinaQuery.execute(query = CREATE_FILE,
            outParamTypes = intArrayOf(OracleTypes.NUMBER, OracleTypes.VARCHAR),
            sessionSetting = sessionUni
        )!!

        return  Pair(id as Number, fileName as String)
    }

    private fun fillAndCheckExistsData(idFile: Number, sessionUni: SessionSetting = SessionSetting(false) ): Boolean {

        AfinaQuery.execute( FILL_DATA_RUTDF, arrayOf(idFile), sessionUni )

        val isExists = selectValueType<Number>(IS_EXISTS_DATA, arrayOf(idFile), sessionUni)?.toInt() ?: 0

        return (isExists > 0)
    }
}

private fun folderGutDfToday() = "$folderReport/${Get440pFiles.todayFolder()}".byFolderExists()

fun errorFolder(): File = "${folderGutDfToday()}/ERROR".byFolderExists()

private val folderReport = "H:/Gu_cb/НБКИ/test".ifTest("C:/Gu_cb/НБКИ/test")

private const val xsd =  "/xsd//gutdf/Main.xsd"

private const val DEL_RUTDF_DATA = "delete from od.PTKB_RUTDF_FILE where ID = ?"

private const val CREATE_FILE = "{ call od.PTKB_RUTDF.createRutdfFileOut(?, ?) }"

private const val FILL_DATA_RUTDF = "{ call od.PTKB_RUTDF.prepareProcessEvents( ? ) }"

private const val IS_EXISTS_DATA =
    "select count(*) from dual where exists (select 1 from od.ptkb_rutdf_main where RUTDF_FILE = ?)"

private const val TEST_SEL_RUTDF_LIST =
    "select f.id, f.file_name from od.ptkb_rutdf_file f where f.date_file >= ? and f.date_file < ? order by 2"