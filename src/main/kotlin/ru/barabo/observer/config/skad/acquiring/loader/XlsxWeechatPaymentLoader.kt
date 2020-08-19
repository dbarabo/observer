package ru.barabo.observer.config.skad.acquiring.loader

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.xlsx.TableLoader
import java.io.File
import java.sql.Timestamp
import java.time.ZoneId

private const val COUNT_COLUMNS: Int = 20

private val logger = LoggerFactory.getLogger(XlsxWeechatPaymentLoader::class.java)

class XlsxWeechatPaymentLoader : TableLoader() {

    private lateinit var sessionSetting: SessionSetting

    private lateinit var headerId: Number

    fun loadPayment(sourceXlsx: File) {
        sessionSetting = AfinaQuery.uniqueSession()

        headerId = AfinaQuery.nextSequence()

        saveHeader(sourceXlsx)

        try {
            loadXlsx(sourceXlsx)
        } catch (e: Exception) {
            logger.error("loadPayment", e)

            AfinaQuery.rollbackFree(sessionSetting)

            throw Exception(e.message)
        }
        AfinaQuery.commitFree(sessionSetting)
    }

    override fun isSkipRow(columns: List<Cell>, stateRow: TypeLine): Boolean {
        return when(stateRow) {
            TypeLine.NOTHING -> !isCheckHeader(columns)
            TypeLine.BODY -> !isBodyOrTail(columns)
            TypeLine.TAIL -> false
            TypeLine.HEADER -> false
        }
    }

    override fun processStateRow(columns: MutableList<Cell>, stateRow: TypeLine): TypeLine {
        return when(stateRow) {
            TypeLine.NOTHING -> {
                columns.clear()
                TypeLine.BODY
            }

            TypeLine.BODY -> {
                processStateBody(columns)
            }
            TypeLine.TAIL -> {
                processStateTail(columns)
            }
            TypeLine.HEADER -> {
                TODO()
            }
        }
    }

    private fun saveHeader(sourceXlsx: File) {

        val params = arrayOf<Any?>(headerId, sourceXlsx.name, Timestamp(sourceXlsx.lastModified()))

        AfinaQuery.execute(query = SQL_INSERT_WEECHAT, params = params, sessionSetting = sessionSetting)
    }

    private fun processStateBody(columns: MutableList<Cell>): TypeLine {
        return if(columns[0].cellType == CellType.NUMERIC) TypeLine.BODY else {
            columns.clear()
            TypeLine.TAIL
        }
    }

    private fun processStateTail(columns: MutableList<Cell>): TypeLine {
        return if(columns[8].cellType == CellType.NUMERIC &&
                columns[10].cellType == CellType.NUMERIC) TypeLine.TAIL else TypeLine.NOTHING
    }

    override fun executeBody(columns: List<Cell>) {

        val params = getRecordParams(columns)

        AfinaQuery.execute(query = SQL_INSERT_RECORD, params = params, sessionSetting = sessionSetting)
    }

    private fun getRecordParams(columns: List<Cell>): Array<Any?> {

        val params = ArrayList<Any?>()

        params += headerId

        for(col in columns) {
            val value: Any = when(col.cellType) {
            CellType.NUMERIC -> {
                when(col.columnIndex) {
                    0, 7, 9 -> col.numericCellValue
                    1 -> Timestamp.from(col.localDateTimeCellValue.atZone(ZoneId.systemDefault()).toInstant())
                    else -> col.numericCellValue * 100
                }
            }

            CellType.STRING -> col.stringCellValue ?: ""

            CellType.BLANK -> ""

            else -> throw Exception("cell type for record unknown CellType=$col")
            }

            logger.error("index=${col.columnIndex} value=$value")

            params += value
        }

        val isReverse = (params.last().toString().indexOf("ОТМЕНА", ignoreCase = true) >= 0)

        params += if(isReverse) TRANSACT_SENDY_REVERSE else TRANSACT_SENDY_PAY

        params += if(isReverse) 1 else 0

        return params.toArray()
    }

    override fun executeTail(columns: List<Cell>) {
        TODO("Not yet implemented")
    }

    override fun executeHeader(columns: List<Cell>) {
        TODO("Not yet implemented")
    }

    private fun isCheckHeader(columns: List<Cell>): Boolean {
        if(columns.size != COUNT_COLUMNS) return false

        for((index, col) in columns.withIndex()) {
            if(col.cellType != CellType.STRING ||
                    col.stringCellValue != (index + 1).toString() ) return false
        }
        return true
    }

    private fun isBodyOrTail(columns: List<Cell>): Boolean {
        if(columns.size != COUNT_COLUMNS) throw Exception("count columns must be $COUNT_COLUMNS")

        if(columns[0].cellType != CellType.NUMERIC) return false

        return checkBody(columns)
//        return when(columns[0].cellType) {
//            CellType.STRING -> !isNameMerchant(columns[0].stringCellValue)
//            CellType.BLANK -> isTotalCell(columns[1])
//            CellType.NUMERIC -> checkBody(columns)
//            else -> throw Exception("first cell Body type must be in STRING, BLANK or NUMERIC")
//        }
    }

    private fun isNameMerchant(cellValue: String): Boolean {
        if( cellValue.indexOf(NAME_MERCHANT_START, ignoreCase = true) == 0) return true

        throw Exception("first cell Body with String type starting only of $NAME_MERCHANT_START")
    }

    private fun isTotalCell(cell: Cell): Boolean {
        if(cell.cellType != CellType.STRING) throw Exception("first cell Blank type only for $NAME_TOTAL_CELL")

        if( cell.stringCellValue?.trim() != NAME_TOTAL_CELL)  return true

        throw Exception("first cell Blank type only for $NAME_TOTAL_CELL")
    }

    private fun checkBody(columns: List<Cell>): Boolean {

        for((index, col) in columns.withIndex()) {
            if(col.cellType != BODY_TYPES[index]) throw Exception("cell Type for cell index=$index must be ${BODY_TYPES[index]}")
        }
        return true
    }
}

private val BODY_TYPES = arrayOf<CellType>(
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.STRING,
        CellType.STRING,
        CellType.STRING,
        CellType.BLANK,
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.STRING,
        CellType.STRING,
        CellType.STRING,
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.NUMERIC,
        CellType.STRING,
        CellType.STRING
)

private const val SQL_INSERT_WEECHAT = "insert into od.PTKB_WEECHAT (ID, FILE_NAME, FILE_TIME) values (?, ?, ?)"

private const val SQL_INSERT_RECORD = """insert into od.PTKB_WEECHAT_RECORD 
(ID, WEECHAT, ROW_ORDER, TRANSACT_DATE, AUTH_ID, TERMINAL_ID, ACCOUNT_30232, ACCOUNT_ESP, AUTH_AMOUNT, EXCHANGE_RATE, AMOUNT_RUR, 
 PERCENT_RATE, COMMIS_AMOUNT_EQUAR, TRANSACT_ID_PC, PAY_SYSTEM, TYPE_OPERATION, COMMIS_AMOUNT_SERVICE, 
 COMMIS_AMOUNT_CLEANING, COMMIS_AMOUNT_EMITENT, COMMIS_AMOUNT_INTERSYSTEM, CLIENT_NAME, DESCRIPTION_REVERSE, TRANSACT_TYPE, IS_REVERSE) 
values (classified.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 
?, ?, ?, ?, ?, ?, ?,
?, ?, ?, ?, ?, ?)"""

private const val NAME_MERCHANT_START = "Наименование мерчанта:"

private const val NAME_TOTAL_CELL = "ИТОГО:"

private const val TRANSACT_SENDY_PAY = "SENDY_PAY"

private const val TRANSACT_SENDY_REVERSE = "SENDY_REVERSE"