package ru.barabo.observer.config.barabo.plastic.turn.task

import org.slf4j.LoggerFactory
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

object LoadAcq : FileFinder, FileProcessor, QuoteSeparatorLoader {

    private val logger = LoggerFactory.getLogger(LoadAcq::class.java)!!

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(LoadRestAccount.hCardIn,"AFP_ACQ20\\d\\d\\d\\d\\d\\d_0226\\.\\d\\d\\d\\d"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка Эквайринг ACQ-файла"

    override fun config(): ConfigTask = PlasticTurnConfig

    private lateinit var fileProcess: File

    override fun processFile(file: File) {

        fileProcess = file

        load(file, Charset.forName("CP1251"))

        checkInitMerchantId(fileId)

        checkSum(fileId)

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        if(!file.delete()) {
            logger.error("FILE IS NOT DELETED $file")
        }
    }

    private var fileId: Any? = null

    private const val SELECT_CURSOR_CHECK_SUM = "{ ? = call od.PTKB_PLASTIC_TURN.selectCheckSumAcq( ? ) }"

    private const val SUBJECT_ERROR = "Ошибка в Чек-сумме файла AFP_ACQ"

    private const val EXEC_TO_ERROR_STATE = "update od.PTKB_ACQ set state = 2 where id = ?"

    private fun bodyError(text :String) =
            "В загруженном файле <${fileProcess.name}> AFP_ACQ.id:<$fileId> не сходится чек-сумма $text"

    private fun checkInitMerchantId(fileId :Any?) {

        AfinaQuery.execute(EXEC_INIT_MERCHANT, arrayOf(fileId))
    }

    private const val EXEC_INIT_MERCHANT =  "{ call od.PTKB_PLASTIC_TURN.checkInitMerchantId( ? ) }"

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

        this.sessionSetting = sessionSetting

        return fileId
    }

    private var sessionSetting: SessionSetting? = null

    override val headerQuery: String = "insert into od.PTKB_ACQ (id, file_receiver, pc_created, " +
            "file_order, period_start, period_end, file_name, CREATED) values (?, ?, ?, ?, ?, ?, ?, ?)"

    override val headerColumns: Map<Int, (String?) -> Any> = mapOf(
            4 to ::parseToString,
            6 to ::parseDateTime,
            7 to ::parseInt,
            8 to ::parseDateTime,
            9 to ::parseDateTime,
            -1 to ::fileProcessName,
            -2 to ::fileLastModified)

    private fun fileProcessName(@Suppress("UNUSED_PARAMETER") value: String?): Any = fileProcess.name

    private fun fileLastModified(@Suppress("UNUSED_PARAMETER") value: String?): Any = java.sql.Timestamp(fileProcess.lastModified())

    override val bodyQuery: String = """
insert into od.PTKB_ACQ_RECORD (id, ACQ, row_order, auth_id, transact_type_fe, card_number,
local_oper, pc_oper, auth_direction, AUTH_CURRENCY, AUTH_AMOUNT,
fee_direction, fee_amount, terminal_id, merchant_id, merchant_name,
merchant_country, merchant_state, merchant_city, merchant_postal, pay_system_id_number,
authorize_approval_code, merchant_category_code, retrieval_ref_number, reversal_flag, description,
IS_PHYSICAL_TERMINAL)
values (classified.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
"""

    override val bodyColumns: Map<Int, (String?) -> Any> = mapOf(
            1 to ::parseInt, // row_order
            2 to ::parseInt, // auth_id
            3 to ::parseToString, // transact_type_fe
            4 to ::parseToString, // card_number
            5 to ::parseDateTime, // local_oper
            6 to ::parseDateTime, // pc_oper
            7 to ::parseToString, // auth_direction
            8 to ::parseInt, // AUTH_CURRENCY
            9 to ::parseInt, // auth_amount
            10 to ::parseToString, // fee_direction
            11 to ::parseInt, // fee_amount
            12 to ::terminalIdWithSave, // terminal_id
            13 to ::parseToString, // merchant_id
            14 to ::parseToString, // merchant_name
            15 to ::parseToString, // merchant_country
            16 to ::merchantLocation, // merchant_state
            17 to ::parseToString, // merchant_city
            18 to ::parseToString, // merchant_postal
            19 to ::parseToString, // pay_system_id_number
            20 to ::parseToString, // authorize_approval_code
            21 to ::parseToString, // merchant_category_code
            22 to ::parseToString, // retrieval_ref_number
            23 to ::parseInt, // reversal_flag
            24 to ::parseToString, // description
            25 to ::parseInt // IS_PHYSICAL_TERMINAL
            )

    override val tailQuery: String = "{ call od.PTKB_PLASTIC_TURN.setTailAcq(?, ?, ?)}"

    override val tailColumns: Map<Int, (String?) -> Any> = mapOf(2 to ::parseInt, 3 to ::parseInt)

    override fun getTypeLine(fields: List<String>, order: Int): TypeLine {
        if (fields.isEmpty()) return TypeLine.NOTHING

        return when (fields[0].uppercase(Locale.getDefault())) {
            "FH" -> TypeLine.HEADER
            "TR" -> TypeLine.BODY
            "FT" -> TypeLine.TAIL

            else -> throw Exception("not found type string ${fields[0]}")
        }
    }

    private var terminalId: String = ""

    private fun terminalIdWithSave(value: String?): Any = (value ?: "").apply { terminalId = this }

    private fun merchantLocation(@Suppress("UNUSED_PARAMETER") value: String?): Any =
        AfinaQuery.selectValue(SELECT_LOCATION, arrayOf(terminalId), sessionSetting!!) ?: ""

    private const val SELECT_LOCATION = """
select MERCHANT_LOCATION
 from od.ptkb_transact_ctl_mtl m
where m.id = (select max(id)
from od.ptkb_transact_ctl_mtl
where terminal_id = ?)"""

}