package ru.barabo.observer.config.cbr.ibank.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.plastic.turn.loader.QuoteSeparatorLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.task.finder.isFind
import java.io.File
import java.nio.charset.Charset
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

object CheckerFiles : QuoteSeparatorLoader {

    private val pathVt = File(PATH_VT)

    private val patternVt = Pattern.compile(REGEXP_VT, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

    private val findFiles: MutableList<String> = ArrayList()

    private val logger = LoggerFactory.getLogger(CheckerFiles::class.java)

    fun findProcess() {

        try {
            processNoError()
        } catch (e: Exception) {
            logger.error("findProcess", e)
        }
    }

    private fun processNoError() {
        val newFiles = pathVt.listFiles { f ->
            (!f.isDirectory) &&
                    (patternVt.isFind(f.name)) &&
                    (!findFiles.contains(f.name.uppercase(Locale.getDefault())))
        }

        if (newFiles.isNullOrEmpty()) return

        for (newFile in newFiles) {
            val isExists = (AfinaQuery.selectValue(
                SELECT_CHECK_FILE,
                arrayOf(newFile.name.uppercase(Locale.getDefault()))
            ) as Number).toInt()

            if (isExists == 0) {
                processFileVt(newFile)
            }
            findFiles.add(newFile.name.uppercase(Locale.getDefault()))
        }
    }

    private lateinit var fileProcess: File

    private fun processFileVt(newFile: File) {
        fileProcess = newFile

        load(newFile, Charset.forName("CP1251"))
    }

    override val headerColumns: Map<Int, (String?) -> Any> = emptyMap()
    override val headerQuery: String? = null

    override val tailColumns: Map<Int, (String?) -> Any> = emptyMap()
    override val tailQuery: String? = null

    override val bodyColumns: Map<Int, (String?) -> Any> = mapOf(
        0 to ::parseToString,
        1 to ::parseNumberSeparator,
        2 to ::parseToUpperString,
        3 to ::parseToString,
        5 to ::parseNumberSeparator,
        -1 to ::fileProcessName,
        -2 to ::dateCreated
    )

    private fun dateCreated(@Suppress("UNUSED_PARAMETER") value: String?): Any = dateFile

    private fun fileProcessName(@Suppress("UNUSED_PARAMETER") value: String?): Any = fileProcess.name.uppercase(Locale.getDefault())

    private fun parseToUpperString(value: String?): Any =
        value?.trim()?.uppercase(Locale.getDefault()) ?: String::class.javaObjectType

    override val bodyQuery: String = INSERT_CLIENT

    private lateinit var dateFile: Timestamp

    override fun getTypeLine(fields: List<String>, order: Int): TypeLine {
        if (fields.isEmpty()) return TypeLine.NOTHING

        return when (fields[0].uppercase(Locale.getDefault())) {
            "START" -> {
                dateFile = fields[1].toDate().toTimestamp()
                TypeLine.NOTHING
            }
            "END" -> TypeLine.NOTHING
            else -> TypeLine.BODY
        }
    }
}

fun parseNumberSeparator(value :String?): Any {
    val length = value?.trim()?.length?:0

    if(length == 0) return Double::class.javaObjectType

    val numberOrder = value?.replace("\\D+".toRegex(), "")

    return numberOrder?.trim()?.toLong()?:0
}

fun String.toDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ofPattern("ddMMyyyy"))

fun LocalDate.toTimestamp(): Timestamp = Timestamp(this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() )

private const val PATH_VT = "H:/Dep_Buh/Зарплатный проект ВТБ/Исходящие файлы/Зарплата"

private const val REGEXP_VT = "Z_0000311595_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d_\\d\\d\\.txt"

const val SELECT_CHECK_FILE = "select od.PTKB_PLASTIC_TURNOUT.checkFileExistsZil( ? ) from dual"

const val INSERT_CLIENT =
    "insert into od.ptkb_zil_client (ID, CODE_ID, AMOUNT, CLIENT, CODE_ID2, AMOUNT_HOLD, FILE_NAME, CREATED) values (classified.nextval, ?, ?, ?, ?, ?, ?, ?)"