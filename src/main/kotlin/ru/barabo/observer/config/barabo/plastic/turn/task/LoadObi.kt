package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.Column
import ru.barabo.observer.config.barabo.plastic.turn.loader.PosLengthLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime

object LoadObi : FileFinder, FileProcessor, PosLengthLoader {
    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(LoadRestAccount.hCardIn,
            "OBI_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\d_0226_GC_FEE"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(6, 0))

    override fun name(): String = "Загрузка OBI/OBM-файла"

    override fun config(): ConfigTask  = PlasticTurnConfig

    override fun processFile(file: File) {

        fileProcess = file

        load(file, Charset.forName("CP1251"))

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()
    }

    private lateinit var fileProcess :File

    private val OBI_DATE_FORMAT = "MMddyyyy"

    private val OBI_DATE_TIME_FORMAT = "MMddyyyyHHmmss"

    fun parseObiDate(date :String?, longFormat:String, shortFormat:String) : Any {
        val length = date?.trim()?.length?:0

        if(length == 0) return LocalDateTime::class.javaObjectType

        val format = if(length < longFormat.length)shortFormat else longFormat

        return Timestamp(SimpleDateFormat(format).parse(date).time)
    }

    private fun parseObiDate(date :String?) : Any = parseObiDate(date, OBI_DATE_TIME_FORMAT, OBI_DATE_FORMAT)

    override val headerColumns: Array<Column> = arrayOf(
            Column(0, 0) { fileProcess.name },
            Column(47, 14, ::parseObiDate)
    )

    fun parseInt(value :String?) :Any {
        val length = value?.trim()?.length?:0

        if(length == 0) return Double::class.javaObjectType

        return if(length < 18) value?.trim()?.toLong()?:0 else BigInteger(value?.trim())
    }

    fun parseToString(value :String?) :Any = value?.trim()?.let{ it } ?: String::class.javaObjectType

    override val bodyColumns: Array<Column> = arrayOf(
            Column(8, 12, ::parseInt),
            Column(20, 32, ::parseToString),
            Column(61, 12, ::parseInt),
            Column(73, 2, ::parseToString),
            Column(75, 3, ::parseInt),
            Column(79, 8, ::parseObiDate),
            Column(88, 16, ::parseInt),
            Column(105, 8, ::parseToString),
            Column(118, 32, ::parseToString),
            Column(150, 8, ::parseToString),
            Column(159, 40, ::parseToString),
            Column(199, 32, ::parseToString),
            Column(231, 32, ::parseToString),
            Column(295, 32, ::parseObiDate),
            Column(327, 32, ::parseInt),
            Column(359, 32, ::parseInt),
            Column(391, 32, ::parseInt),
            Column(423, 32, ::parseInt),
            Column(455, 12, ::parseToString),
            Column(467, 8, ::parseToString),
            Column(475, 15, ::parseToString),
            Column(498, 220, ::parseToString)
    )

    override val tailColumns: Array<Column> = emptyArray()

    override val headerQuery: String? = "insert into od.PTKB_OBI (id, file_name, PC_CREATE) values (?, ?, ?)"

    override val bodyQuery: String? = ("insert into od.PTKB_TRANSACT_OBI (id, obi, RECORD_NUMBER, account_number, " +
            "account_amount, dr_cr, cur_account, pc_process, ref_oper_id, trans_bo, institute_corr, trans_fe, " +
            "trans_place, card_number, mcc_code, trans_date, trans_amount, settlement_amount, cur_trans, " +
            "cur_settlement, trans_group_id, terminal_id, merchant_id, DESCRIPTION) values (classified.nextval, ?," +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

    override val tailQuery: String? = null

    override fun getTypeLine(line: String, order: Int): TypeLine {
        if(line.length < 6) return TypeLine.NOTHING

        return when (line.substring(0, 6).toUpperCase()) {
            "RCTP01"-> TypeLine.HEADER
            "RCTP10"-> TypeLine.BODY
            "RCTP02"-> TypeLine.TAIL

            else-> throw Exception("not found type string $line")
        }
    }
}