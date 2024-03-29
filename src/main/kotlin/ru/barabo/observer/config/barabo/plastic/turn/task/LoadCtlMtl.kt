package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.QuoteSeparatorLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.util.*

object LoadCtlMtl : CtlLoader(), FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(LoadRestAccount.hCardIn,
            "(C|M)TL.*_0226.*"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка CTL/MTL-файла"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun processFile(file: File) {

        loadCtlMtl(file)
    }
}

open class CtlLoader : QuoteSeparatorLoader {

    private lateinit var fileProcess: File

    private var isCtl: Boolean = false

    private var fileId: Any? = null

    override val tailQuery: String? = "{ call od.PTKB_PLASTIC_TURN.setTailCtlMtl(?, ?, ?)}"

    override val tailColumns: Map<Int, (String?) -> Any> = mapOf(2 to ::parseInt, 3 to ::parseInt)

    override val headerColumns: Map<Int, (String?) -> Any> = mapOf(
            2 to ::parseToString,
            4 to ::parseToString,
            5 to ::parseToString,
            6 to ::parseDateTime,
            7 to ::parseInt,
            8 to ::parseDateTime,
            -1 to { _: String? -> fileProcess.name})

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

    override val headerQuery: String? = "insert into od.PTKB_CTL_MTL (id, type, file_receiver, " +
            "file_branch_receiver, PC_CREATED, file_order, pc_process, file_name) values (?, ?, ?, ?, ?, ?, ?, ?)"

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

    override fun generateHeaderSequence(fields :List<String>, sessionSetting : SessionSetting) :Any? {

        fileId = super.generateHeaderSequence(fields, sessionSetting)

        return fileId
    }

    override fun getTypeLine(fields: List<String>, order: Int): TypeLine {
        if (fields.isEmpty()) return TypeLine.NOTHING

        return when (fields[0].uppercase(Locale.getDefault())) {
            "FH" -> {
                isCtl = fields[2].uppercase(Locale.getDefault()) == "CTL"

                TypeLine.HEADER
            }
            "TR" -> TypeLine.BODY
            "FT" -> TypeLine.TAIL

            else -> throw Exception("not found type string ${fields[0]}")
        }
    }

    private fun indicator(value :String?) :Any = if(isCtl) parseToString(value) else ""

    private fun checkSumStatus(fileId :Any?) {

        val values = AfinaQuery.selectCursor(SELECT_CURSOR_CHECK_SUM, arrayOf(fileId))[0]

        var textCheck: String? = null

        if( (values[0] as? Number)?.toInt() != (values[1] as? Number)?.toInt() ) {
            textCheck = "Не сходится кол-во транзакций в Чек-сумме и файле в Чек-сумме:${values[0]} в файле:${values[1]}\n"
        }

        if( (values[2] as? Number)?.toLong() != (values[3] as? Number)?.toLong() ) {
            textCheck += "Не сходится сумма транзакций в Чек-сумме и файле в Чек-сумме:${values[2]} в файле:${values[3]}\n"
        }

        textCheck?.let {
            AfinaQuery.execute(EXEC_TO_STATE, arrayOf(ERROR_STATUS, fileId))

            BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_ERROR, body = bodyError(it))
        }
    }

    private fun bodyError(text :String) =
            "В загруженном файле <${fileProcess.name}> CTL.id:<$fileId> \n $text"

    protected fun loadCtlMtl(file: File) {

        fileProcess = file

        load(file, Charset.forName("CP1251"))

        checkSumStatus(fileId)

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()
    }
}

private const val ERROR_STATUS = 2

fun parseDateTime(date: String?): Any = parseObiDate(date, DATE_TIME_FORMAT, DATE_FORMAT)

private const val DATE_TIME_FORMAT = "yyyyMMddHHmmss"

private const val DATE_FORMAT = "yyyyMMdd"

private const val SELECT_CURSOR_CHECK_SUM = "{ ? = call od.PTKB_PLASTIC_TURN.selectCheckSum( ? ) }"

private const val SUBJECT_ERROR = "Ошибка в Чек-сумме файла CTL/MTL"

const val EXEC_TO_STATE = "update od.PTKB_CTL_MTL set state = ? where id = ?"