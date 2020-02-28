package ru.barabo.observer.config.jzdo.upay.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.loader.QuoteSeparatorLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadAcq
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.barabo.plastic.turn.task.parseDateTime
import ru.barabo.observer.config.jzdo.upay.UPayConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.nio.charset.Charset
import java.time.Duration

object LoadAcqAdvUPay : FileFinder, FileProcessor, QuoteSeparatorLoader {

    override fun name(): String = "Загрузка UPay ADV-файла"

    override fun config(): ConfigTask = UPayConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(UncryptoUPay::uncryptoFolder,
            "ZKACQ_ADV_.*\\.\\d\\d\\d"))

    private lateinit var fileProcess: File

    override fun processFile(file: File) {

        fileProcess = file

        load(file, Charset.forName("CP1251"))

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")
        file.copyTo(moveFile, true)
        file.delete()
    }

    override fun getTypeLine(fields: List<String>, order: Int): TypeLine {
        if(fields.isEmpty() ) return TypeLine.NOTHING

        return when (fields[0].toUpperCase()) {
            "H"->  TypeLine.HEADER
            "ACQ_ADV"-> TypeLine.BODY
            "T"-> TypeLine.TAIL

            else-> throw Exception("not found type string ${fields[0]}")
        }
    }

    override val headerQuery: String? = "insert into od.PTKB_ACQ (id, file_receiver, pc_created, " +
            "file_order, period_start, period_end, file_name, CREATED) values (?, ?, ?, ?, ?, ?, ?, ?)"

    override val headerColumns: Map<Int, (String?) -> Any> = mapOf(
            1 to LoadAcq::parseToString,
            4 to ::parseDateWithSave,
            3 to LoadAcq::parseInt,
            -1 to {_: String? -> created},
            -2 to {_: String? -> created},
            -3 to {_: String? -> fileProcess.name },
            -4 to {_ -> java.sql.Timestamp(fileProcess.lastModified())} )

    private lateinit var created: Any

    private fun parseDateWithSave(value: String?): Any = parseDateTime(value).apply { created = this }

    override val tailQuery: String? = "{ call od.PTKB_PLASTIC_TURN.setTailAcq(?, ?, ?)}"

    override val tailColumns: Map<Int, (String?) -> Any> = mapOf(
            1 to LoadAcq::parseInt,
            -1 to {_: String? -> 0 } )

    override val bodyQuery: String? = """
insert into od.PTKB_ACQ_RECORD (id, ACQ, row_order, auth_id, transact_type_fe, card_number,
local_oper, pc_oper, auth_direction, AUTH_CURRENCY, AUTH_AMOUNT,
fee_amount, terminal_id, merchant_id, merchant_name, merchant_city, 
pay_system_id_number, authorize_approval_code, merchant_category_code, retrieval_ref_number, reversal_flag, 
IS_PHYSICAL_TERMINAL)
values (classified.nextval, ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?,  ?)"""

    override val bodyColumns: Map<Int, (String?) -> Any> = mapOf(
            16 to LoadAcq::parseInt, // row_order
            1 to LoadAcq::parseInt, // auth_id
            13 to ::operTypeToFe, // transact_type_fe
            2 to LoadAcq::parseToString, // card_number
            3 to ::parseDateWithSave, // local_oper
            -1 to {_: String? -> created}, // pc_oper
            15 to LoadAcq::parseToString, // auth_direction
            18 to LoadAcq::parseInt, // AUTH_CURRENCY
            19 to LoadAcq::parseInt, // auth_amount
            20 to LoadAcq::parseInt, // fee_amount
            7 to ::terminalIdWithSave, // terminal_id
            8 to LoadAcq::parseToString, // merchant_id
            -2 to ::merchantNameByTerminalId, // merchant_name
            -3 to ::merchantAddressByTerminalId, // merchant_city
            -4 to { _: String? -> "9960" }, // pay_system_id_number пока только Upay international.ZK
            6 to LoadAcq::parseToString, // authorize_approval_code
            9 to LoadAcq::parseToString, // merchant_category_code
            5 to LoadAcq::parseToString, // retrieval_ref_number
            14 to ::trnTypeToReverse, // reversal_flag
            -5 to { _: String? -> 1 } // IS_PHYSICAL_TERMINAL
    )

    private lateinit var terminalId: String

    private fun terminalIdWithSave(value: String?): Any = (value ?: "").apply { terminalId = this }

    private fun trnTypeToReverse(value: String?): Any = if(value == "D") 0 else 1

    private fun merchantAddressByTerminalId(skip: String?): Any = AfinaQuery.selectValue(SELECT_MERCHANT_ADDRESS, arrayOf(terminalId))!!

    private fun merchantNameByTerminalId(skip: String?): Any = AfinaQuery.selectValue(SELECT_MERCHANT_NAME, arrayOf(terminalId))!!

    private const val SELECT_MERCHANT_NAME = "select od.PTKB_PLASTIC_TURN.getMerchantNameByTerminal( ? ) from dual"

    private const val SELECT_MERCHANT_ADDRESS = "select od.PTKB_PLASTIC_TURN.getMerchantAddresByTerminal( ? ) from dual"

    private fun operTypeToFe(value: String?): Any {

        val codeOpType = value?.trim()?.toInt() ?: throw Exception("OPTYPE field must be Integer OPTYPE=$value")

        return when(codeOpType) {
            40 ->  "774" // безнал. оплата
            43 -> "700" // снятие наличных
            57, 64 -> "775" // возврат по безналу
            else -> throw Exception("unknown OPTYPE=$codeOpType")
        }
    }
}