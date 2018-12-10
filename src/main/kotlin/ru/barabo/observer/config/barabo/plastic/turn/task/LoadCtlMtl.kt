package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.QuoteSeparatorLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadObi.parseObiDate
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.util.regex.Pattern

object LoadCtlMtl : FileFinder, FileProcessor, QuoteSeparatorLoader {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(LoadRestAccount.hCardIn,
            "(C|M)TL.*_0226.*"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS)

    override fun name(): String = "Загрузка CTL/MTL-файла"

    override fun config(): ConfigTask = PlasticTurnConfig

    override var patternCsvQuote: Pattern? = null

    private lateinit var fileProcess :File

    override fun processFile(file: File) {

        fileProcess = file

        load(file, Charset.forName("CP1251") )

        checkSum(fileId)

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()
    }

    private var fileId :Any? = null

    private const val SELECT_CURSOR_CHECK_SUM = "{ ? = call od.PTKB_PLASTIC_TURN.selectCheckSum( ? ) }"

    private const val SUBJECT_ERROR = "Ошибка в Чек-сумме файла CTL/MTL"

    private const val EXEC_TO_ERROR_STATE = "update od.PTKB_CTL_MTL set state = 2 where id = ?"

    private fun bodyError(text :String) =
            "В загруженном файле <${fileProcess.name}> CTL.id:<$fileId> не сходится чек-сумма $text"

    private fun checkSum(fileId :Any?) {

        val values = AfinaQuery.selectCursor(SELECT_CURSOR_CHECK_SUM, arrayOf(fileId))[0]

        var textCheck :String? = null

        if( (values[0] as? Number)?.toInt() != (values[1] as? Number)?.toInt() ) {
            textCheck = "Не сходится кол-во транзакций в Чек-сумме и файле в Чек-сумме:${values[0]} в файле:${values[1]}\n"
        }

        if( (values[2] as? Number)?.toLong() != (values[3] as? Number)?.toLong() ) {
            textCheck += "Не сходится кол-во сумма транзакций в Чек-сумме и файле в Чек-сумме:${values[2]} в файле:${values[3]}\n"
        }

        textCheck?.apply {
            AfinaQuery.execute(EXEC_TO_ERROR_STATE, arrayOf(fileId))

            BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_ERROR, body = bodyError(this))
        }
    }

    override fun generateHeaderSequence(fields :List<String>, sessionSetting : SessionSetting) :Any? {

        fileId = super.generateHeaderSequence(fields, sessionSetting)

        return fileId
    }

    override val headerQuery: String? = "insert into od.PTKB_CTL_MTL (id, type, file_receiver, " +
            "file_branch_receiver, PC_CREATED, file_order, pc_process, file_name) values (?, ?, ?, ?, ?, ?, ?, ?)"

    private const val DATE_TIME_FORMAT = "yyyyMMddHHmmss"

    private const val DATE_FORMAT = "yyyyMMdd"

    fun parseDateTime(date :String?): Any = parseObiDate(date, DATE_TIME_FORMAT, DATE_FORMAT)

    override val headerColumns: Map<Int, (String?) -> Any> = mapOf(
     2 to ::parseToString,
            4 to ::parseToString,
            5 to ::parseToString,
            6 to ::parseDateTime,
            7 to ::parseInt,
            8 to ::parseDateTime,
            -1 to { _: String? -> fileProcess.name})

    override val bodyQuery: String? = ("insert into od.PTKB_TRANSACT_CTL_MTL (id, ptkb_ctl_mtl, row_order, " +
            "groupid_transact, id_transact, authorize_id, transact_pair_id, transact_type_bo, transact_type_fe, " +
            "account_number, card_number, transact_local_oper, transact_pc_oper, transact_direction, transact_cur, " +
            "settlement_cur, account_cur, transact_amount, settlement_amount, account_amount, merchant_id, " +
            "merchant_name, merchant_location, transact_country, transact_state, transact_city, transact_postal, " +
            "terminal_id, authorize_approval_code, transact_pc_process, settlement_process, pay_system_id_number, " +
            "merchant_category_code, acquirer_ref_number, retrieval_ref_number, transact_channel, " +
            "authorize_condition, description, transact_ext_city, electronic_commerc_indicator, " +
            //"term_num, " +
            "authorize_data) values (classified.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            //"?, " +
            "?)")

    private fun indicator(value :String?) :Any = if(isCtl)parseToString(value) else ""

    override val bodyColumns: Map<Int, (String?) -> Any> = mapOf(
            1 to ::parseInt,
            2 to ::parseToString,
            3 to ::parseInt,
            4 to ::parseInt,
            5 to ::parseInt,
            6 to ::parseToString,
            7 to ::parseToString,
            8 to ::parseToString,
            9 to ::parseToString,
            10 to ::parseDateTime,
            11 to ::parseDateTime,
            12 to ::parseToString,
            13 to ::parseInt,
            14 to ::parseInt,
            15 to ::parseInt,
            16 to ::parseInt,
            17 to ::parseInt,
            18 to ::parseInt,
            19 to ::parseToString,
            20 to ::parseToString,
            21 to ::parseToString,
            22 to ::parseToString,
            23 to ::parseToString,
            24 to ::parseToString,
            25 to ::parseToString,
            26 to ::parseToString,
            27 to ::parseToString,
            28 to ::parseDateTime,
            29 to ::parseDateTime,
            30 to ::parseToString,
            31 to ::parseToString,
            32 to ::parseInt,
            33 to ::parseToString,
            34 to ::parseToString,
            35 to ::parseToString,
            36 to ::parseToString,
            37 to ::parseToString,
            38 to ::indicator,
            //38 to ::termNum,
            39 to ::parseToString
            )

    override val tailQuery: String? = "{ call od.PTKB_PLASTIC_TURN.setTailCtlMtl(?, ?, ?)}"

    override val tailColumns: Map<Int, (String?) -> Any> = mapOf(2 to ::parseInt, 3 to ::parseInt)

    private var isCtl:Boolean = false

    override fun getTypeLine(fields: List<String>, order: Int): TypeLine {
        if(fields.isEmpty() ) return TypeLine.NOTHING

        return when (fields[0].toUpperCase()) {
            "FH"-> {
                isCtl = fields[2].toUpperCase() == "CTL"

                TypeLine.HEADER
            }
            "TR"-> TypeLine.BODY
            "FT"-> TypeLine.TAIL

            else-> throw Exception("not found type string ${fields[0]}")
        }
    }
}