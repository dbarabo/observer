package ru.barabo.observer.config.barabo.plastic.turn.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.QuoteSeparatorLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Duration
import java.time.LocalTime
import java.util.regex.Pattern
import kotlin.math.roundToLong

object ReportTransListLoader  : FileFinder, FileProcessor, QuoteSeparatorLoader {

    override fun name(): String = "Загрузка файла ReportTransList*.csv"

    override fun config(): ConfigTask = PlasticTurnConfig

    override val fileFinderData: List<FileFinderData> = listOf(
        FileFinderData(hCardBin,"ReportTransList.*\\.csv")
    )

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.WORK_ONLY,
        workTimeFrom = LocalTime.of(8, 0), workTimeTo = LocalTime.of(20, 0),
        executeWait = Duration.ofSeconds(1))

    private lateinit var fileProcess: File

    override fun processFile(file: File) {

        fileProcess = file

        load(file, Charset.forName("CP1251"))

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        if(!file.delete()) {
            logger.error("FILE IS NOT DELETED $file")
        }
    }

    override val headerQuery: String = "insert into od.PTKB_BANKOMAT (id, FILE_NAME) values (?, ?)"

    override val headerColumns: Map<Int, (String?) -> Any> = mapOf(
        -1 to { fileProcess.name}
    )

    override fun getTypeLine(fields: List<String>, order: Int): TypeLine {
        if(fields.isEmpty() ||  order == 0) return TypeLine.NOTHING

        return if(order == 1) TypeLine.HEADER else TypeLine.BODY
    }

    override val tailColumns: Map<Int, (String?) -> Any> = emptyMap()

    override val tailQuery: String? = null

    override val bodyQuery: String = "{ call od.PTKB_PLASTIC_TURN.saveReportTransRecord(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }"

    override val bodyColumns: Map<Int, (String?) -> Any> = mapOf(

        0 to ::parseToString,  // terminal_id
        1 to ::parseToString, // card_pan
        2 to ::currencyToInt, // AMOUNT_KOPEIKA
        5 to ::parseDateTimeAuth, // AUTH_DATE
        8 to ::parseInt, // TRANSACT_ID
       10 to ::parseToString, // AUTH_CODE
       11 to ::parseInt, // RRN
       12 to ::parseOperType, // OPERTYPE
       13 to ::parseToString, // ERR_TEXT
       14 to ::parseDateTimeTransact, // TRANSACT_DATE
       15 to ::parseInt, // REPORT_ID
       16 to ::parseDateTimeReport, // REPORT_DATE
       21 to ::parseInt, // ROUTE
       22 to ::parseColor // color
    )

    private fun parseColor(value: String?): Any {
        val length = value?.trim()?.length?:0

        if(length == 0) return Double::class.javaObjectType

        return when(value?.trim()?.toUpperCase()) {
            "WHITE" -> 0
            "RED" -> 1
            "GREEN" -> 2
            else -> 9
        }
    }

    private fun parseOperType(value: String?): Any =
        patternOperType.matcher(value!!).takeIf { it.find() }?.group(1)?.trim()?.toInt() ?: 0

    private fun currencyToInt(value: String?): Any = (formatCurrency.parse(value).toDouble() * 100).roundToLong()

    private fun parseDateTimeAuth(date: String?): Any = parseObiDate(date, DATE_TIME_FORMAT_AUTH, DATE_FORMAT_AUTH)

    private fun parseDateTimeTransact(date: String?): Any = parseObiDate(date, DATE_TIME_FORMAT_TRANS, DATE_FORMAT_TRANS)

    private fun parseDateTimeReport(date: String?): Any = parseObiDate(date, DATE_TIME_ZONE_FORMAT_REPORT, DATE_TIME_FORMAT_REPORT)
}

private const val DATE_TIME_ZONE_FORMAT_REPORT = "dd.MM.yy HH:mm:ss zzzz"

private const val DATE_TIME_FORMAT_REPORT = "dd.MM.yy HH:mm:ss"

private const val DATE_TIME_FORMAT_TRANS = "dd-MM-yyyy HH:mm:ss"

private const val DATE_FORMAT_TRANS = "dd-MM-yyyy"

private const val DATE_TIME_FORMAT_AUTH = "dd.MM.yyyy HH:mm:ss"

private const val DATE_FORMAT_AUTH = "dd.MM.yyyy"

private val patternOperType = Pattern.compile("\\[([0-9]+)\\]")

private val formatCurrency = DecimalFormat("#,###.##",
    DecimalFormatSymbols().apply {
    decimalSeparator = ','
    groupingSeparator = ' '
})

private val logger = LoggerFactory.getLogger(ReportTransListLoader::class.java)!!