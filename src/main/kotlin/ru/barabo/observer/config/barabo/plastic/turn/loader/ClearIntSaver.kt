package ru.barabo.observer.config.barabo.plastic.turn.loader

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId

fun saveClearIntToDB(clearIntInfo: ClearIntInfo, file: File) = ClearIntSaverImpl().saveInfo(clearIntInfo, file)

interface ClearIntSaver {
    fun saveInfo(clearIntInfo: ClearIntInfo, file: File)
}

private val logger = LoggerFactory.getLogger(ClearIntSaver::class.java)

private class ClearIntSaverImpl : ClearIntSaver {
    override fun saveInfo(clearIntInfo: ClearIntInfo, file: File) {

        if(clearIntInfo.isEmpty()) return

        val sessionSetting = AfinaQuery.uniqueSession()

        try {

            processSave(clearIntInfo, file, sessionSetting)

        } catch (e: Exception) {

            logger.error("saveInfo", e)

            AfinaQuery.rollbackFree(sessionSetting)

            throw Exception(e.message)
        }

        AfinaQuery.commitFree(sessionSetting)
    }

    private fun processSave(clearIntInfo: ClearIntInfo, file: File, sessionSetting: SessionSetting) {

        val mainClearIntId = saveMain(clearIntInfo, file, sessionSetting)

        for(header in clearIntInfo.headers) {
            saveHeader(mainClearIntId, header, clearIntInfo.date!!, sessionSetting)
        }
    }

    private fun saveMain(clearIntInfo: ClearIntInfo, file: File, sessionSetting: SessionSetting): Number {

        val mainId = AfinaQuery.nextSequence(sessionSetting)

        val dateClearInfo = Timestamp.from(clearIntInfo.date!!.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val dateFile = Timestamp(SimpleDateFormat("yyyyMMdd").parse(file.name.substringAfter('_').substringBefore('_')).time)

        val params = arrayOf<Any?>(mainId, file.name, dateClearInfo,  dateFile)

        AfinaQuery.execute(INSERT_MAIN, params, sessionSetting)

        return mainId
    }

    private fun saveHeader(mainClearIntId: Number, header: HeaderInfo, dateReport: LocalDate, sessionSetting: SessionSetting) {
        if(header.h1.isBlank() && header.h2.isBlank() ) {
            throw Exception("Заголовки пустые в записи $header")
        }

        val (typePayInfo, paySystem, payId) = savePayHeader(mainClearIntId, header, sessionSetting)

        if(header.table.isEmptyTable() ) return

        when(typePayInfo) {
            TypePayInfo.TransactTotal -> saveTransactTotal(mainClearIntId, header.table, payId, sessionSetting)
            TypePayInfo.FeePay -> saveFeePay(header.table, payId, sessionSetting)
            TypePayInfo.ConverseFromRur -> saveConverseFromRur(header.table, payId, sessionSetting)
            TypePayInfo.SettlementPaySystem -> saveSettlementPaySystem(header.table, payId, sessionSetting)
            TypePayInfo.SettlementCorrespondent -> saveSettlementCorrespondent(header.table, paySystem, payId, sessionSetting)
            TypePayInfo.PayCalcVisa -> savePayCalcVisa(header.table, payId, dateReport, sessionSetting)
            TypePayInfo.NoCalcOper -> saveNoCalcOper(header.table, payId, dateReport, sessionSetting)

            TypePayInfo.DetailFeeVisa -> errorIfFindTableTypeInfo(TypePayInfo.DetailFeeVisa, paySystem)
            TypePayInfo.ConverseToRur -> errorIfFindTableTypeInfo(TypePayInfo.ConverseToRur, paySystem)
            TypePayInfo.ClaimMessage -> errorIfFindTableTypeInfo(TypePayInfo.ClaimMessage, paySystem)
        }
    }

    private fun saveNoCalcOper(table: HtmlTable, payId: Number, dateReport: LocalDate, sessionSetting: SessionSetting) {
        val settlementDate = Timestamp.from(dateReport.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val typeCalc = table.findColumnIndex(DESCRIPTION_VISA, TypePayInfo.NoCalcOper)

        val indexCount = table.findColumnIndex(COUNT_OPER, TypePayInfo.NoCalcOper)

        val indexDirection = table.findColumnIndex(DIRECTION_MC, TypePayInfo.NoCalcOper)

        val indexCurrency = table.findColumnIndex(CURRENCY, TypePayInfo.NoCalcOper)

        val indexAmount = table.findColumnIndex(AMOUNT, TypePayInfo.PayCalcVisa)

        for (row in table.rows) {

            val params = arrayOf<Any?>(payId,
                settlementDate,
                table.cellValue(row, indexAmount),
                table.cellValue(row, indexCurrency),
                table.cellValue(row, indexCount),
                typeCalc,
                table.cellValue(row, indexDirection)
            )
            AfinaQuery.execute(INSERT_PAY_CALC_MC, params, sessionSetting)
        }
    }

    private fun savePayCalcVisa(table: HtmlTable, payId: Number, dateReport: LocalDate, sessionSetting: SessionSetting) {

        val settlementDate = Timestamp.from(dateReport.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val indexDescription = table.findColumnIndex(DESCRIPTION_VISA, TypePayInfo.PayCalcVisa)

        val indexCurrency = table.findColumnIndex(CURRENCY, TypePayInfo.PayCalcVisa)

        val indexCount = table.findColumnIndex(COUNT_VISA, TypePayInfo.PayCalcVisa)

        val indexAmount = table.findColumnIndex(AMOUNT, TypePayInfo.PayCalcVisa)

        for (row in table.rows) {

            val description = table.cellValue(row, indexDescription).toString()

            val isPay = if(description.indexOf(IS_PAY) == 0) 1 else 0

            val typeCalc = when {
                description.indexOf(IS_POS) >= 0 -> 0
                description.indexOf(IS_ATM) >= 0 -> 1
                else -> 2
            }

            val params = arrayOf<Any?>(payId,
                settlementDate,
                isPay,
                table.cellValue(row, indexAmount),
                table.cellValue(row, indexCurrency),
                table.cellValue(row, indexCount),
                description,
                typeCalc
            )
            AfinaQuery.execute(INSERT_PAY_CALC, params, sessionSetting)
        }
    }

    private fun saveConverseFromRur(table: HtmlTable, payId: Number, sessionSetting: SessionSetting) {

        val indexDateSettlement = table.findColumnIndex(DATE_SETTLEMENT, TypePayInfo.ConverseFromRur)

        val indexCurrencyTo = table.findColumnsIndex(listOf(CURRENCY, SETTLEMENT_CURRENCY), TypePayInfo.ConverseFromRur)

        val indexAmountTo = table.findColumnsIndex(listOf(AMOUNT, SETTLEMENT_AMOUNT), TypePayInfo.ConverseFromRur)

        val indexAmountFrom = table.findColumnsIndex(listOf(OPER_AMOUNT, AMOUNT_RUR), TypePayInfo.ConverseFromRur)

        val indexCurrencyFrom = if(AMOUNT_RUR in table.headers) -1 else table.findColumnIndex(OPER_CURRENCY, TypePayInfo.ConverseFromRur)

        for (row in table.rows) {
            val params = arrayOf<Any?>(payId,
                table.cellValue(row, indexDateSettlement),
                table.cellValue(row, indexCurrencyTo),
                table.cellValue(row, indexAmountTo),
                if(indexCurrencyFrom == -1) 810 else table.cellValue(row, indexCurrencyFrom),
                table.cellValue(row, indexAmountFrom)
            )

            AfinaQuery.execute(INSERT_PAY_CONVERSE, params, sessionSetting)
        }
    }

    private fun saveFeePay(table: HtmlTable, payId: Number, sessionSetting: SessionSetting) {

        val indexDateSettlement = table.findColumnIndex(DATE_SETTLEMENT, TypePayInfo.FeePay)

        val indexDirection = table.findColumnsIndex(listOf(DIRECTION, DIRECTION_MC), TypePayInfo.FeePay)

        val indexSettlementAmount = table.findColumnIndex(AMOUNT, TypePayInfo.FeePay)

        val indexSettlementCurrency = table.findColumnIndex(CURRENCY, TypePayInfo.FeePay)

        val indexDescription = table.findColumnsIndex(listOf(DESCRIPTION, DESCRIPTION_VISA), TypePayInfo.FeePay)

        for (row in table.rows) {
            val params = arrayOf<Any?>(payId,
                table.cellValue(row, indexDateSettlement),
                table.cellValue(row, indexDirection),
                table.cellValue(row, indexSettlementAmount),
                table.cellValue(row, indexSettlementCurrency),
                table.cellValue(row, indexDescription)
            )

            AfinaQuery.execute(INSERT_PAY_INFO, params, sessionSetting)
        }
    }

    private fun saveSettlementPaySystem(table: HtmlTable, payId: Number, sessionSetting: SessionSetting) {

        val indexDateSettlement = table.findColumnIndex(DATE_SETTLEMENT, TypePayInfo.SettlementPaySystem)

        val indexDirection = table.findColumnIndex(DIRECTION_MC, TypePayInfo.SettlementPaySystem)

        val indexSettlementAmount = table.findColumnIndex(AMOUNT, TypePayInfo.SettlementPaySystem)

        val indexSettlementCurrency = table.findColumnIndex(CURRENCY, TypePayInfo.SettlementPaySystem)

        val indexDescription = table.findColumnIndex(DESCRIPTION_PAY, TypePayInfo.SettlementPaySystem)

        for (row in table.rows) {
            val params = arrayOf<Any?>(payId,
                table.cellValue(row, indexDateSettlement),
                table.cellValue(row, indexDirection),
                table.cellValue(row, indexSettlementAmount),
                table.cellValue(row, indexSettlementCurrency),
                table.cellValue(row, indexDescription)
            )

            AfinaQuery.execute(INSERT_PAY_INFO, params, sessionSetting)
        }
    }

    private fun saveSettlementCorrespondent(table: HtmlTable, paySystem: PaySystem, payId: Number, sessionSetting: SessionSetting) {

        val indexDateSettlement = table.findColumnIndex(DATE_SETTLEMENT, TypePayInfo.SettlementCorrespondent)

        val indexDirection = table.findColumnIndex(DIRECTION_MC, TypePayInfo.SettlementCorrespondent)

        val indexSettlementAmount = table.findColumnIndex(AMOUNT, TypePayInfo.SettlementCorrespondent)

        val indexSettlementCurrency = table.findColumnIndex(CURRENCY, TypePayInfo.SettlementCorrespondent)

        val indexDescription = if(paySystem == PaySystem.VISA)-1
            else table.findColumnIndex(DESCRIPTION_PAY, TypePayInfo.SettlementCorrespondent)

        for (row in table.rows) {
            val params = arrayOf<Any?>(payId,
                table.cellValue(row, indexDateSettlement),
                table.cellValue(row, indexDirection),
                table.cellValue(row, indexSettlementAmount),
                table.cellValue(row, indexSettlementCurrency),
                if (indexDescription == -1) "" else table.cellValue(row, indexDescription)
            )

            AfinaQuery.execute(INSERT_PAY_INFO, params, sessionSetting)
        }
    }

    private fun saveTransactTotal(mainClearIntId: Number, table: HtmlTable, payId: Number, sessionSetting: SessionSetting) {

        val indexDateSettlement = table.findColumnIndex(DATE_SETTLEMENT, TypePayInfo.TransactTotal)

        val indexTypeTransact = table.findColumnIndex(TYPE_TRANSACT, TypePayInfo.TransactTotal)

        val indexDirection = table.findColumnIndex(DIRECTION, TypePayInfo.TransactTotal)

        val indexCount = table.findColumnIndex(COUNT, TypePayInfo.TransactTotal)

        val indexAccountAmount = table.findColumnIndex(ACCOUNT_AMOUNT, TypePayInfo.TransactTotal)

        val indexAccountCurrency = table.findColumnIndex(ACCOUNT_CURRENCY, TypePayInfo.TransactTotal)

        val indexSettlementAmount = table.findColumnIndex(SETTLEMENT_AMOUNT, TypePayInfo.TransactTotal)

        val indexSettlementCurrency = table.findColumnIndex(SETTLEMENT_CURRENCY, TypePayInfo.TransactTotal)

        val indexForSettlementAmount = table.findColumnIndex(FOR_SETTLEMENT_AMOUNT, TypePayInfo.TransactTotal)

        for (row in table.rows) {
            val params = arrayOf<Any?>(mainClearIntId, payId,
                table.cellValue(row, indexDateSettlement),
                table.cellValue(row, indexTypeTransact),
                table.cellValue(row, indexDirection),
                table.cellValue(row, indexCount),
                table.cellValue(row, indexAccountAmount),
                table.cellValue(row, indexAccountCurrency),
                table.cellValue(row, indexSettlementAmount),
                table.cellValue(row, indexSettlementCurrency),
                table.cellValue(row, indexForSettlementAmount),
                table.cellValue(row, indexSettlementCurrency)
            )

            AfinaQuery.execute(INSERT_TRANSACT_TOTAL, params, sessionSetting)
        }
    }

    private fun errorIfFindTableTypeInfo(typePayInfo: TypePayInfo, paySystem: PaySystem) {
        throw Exception("Найдена непустая таблица для $typePayInfo для платежной системы $paySystem")
    }

    private fun savePayHeader(mainClearIntId: Number, header: HeaderInfo,
                              sessionSetting: SessionSetting): Triple<TypePayInfo, PaySystem, Number> {

        val typePayInfo = TypePayInfo.typePayInfoByHeaders(header.h1, header.h2, header.paySystem)

        val paySystem = PaySystem.paySystemByLabel(header.paySystem)

        val payId = AfinaQuery.nextSequence(sessionSetting)

        val header1 = if(header.h1.isBlank()) header.h2 else header.h1

        val header2 = if(header.h1.isBlank())"" else header.h2

        val params = arrayOf<Any?>(payId, mainClearIntId, paySystem.dbValue, paySystem.isNspk, header1, header2, typePayInfo.dbValue)

        AfinaQuery.execute(INSERT_PAY, params, sessionSetting)

        return Triple(typePayInfo, paySystem, payId)
    }
}

private fun HtmlTable.findColumnIndex(columnName: String, typePayInfoTable: TypePayInfo): Int {
    return headers.withIndex().firstOrNull { it.value == columnName }?.index
        ?: throw Exception("для таблицы $typePayInfoTable не найден столбец $columnName")
}

private fun HtmlTable.findColumnsIndex(columnsName: List<String>, typePayInfoTable: TypePayInfo): Int {
    return headers.withIndex().firstOrNull { it.value in columnsName }?.index
        ?: throw Exception("для таблицы $typePayInfoTable не найден столбец из $columnsName")
}

private const val IS_ATM = "(АТМ-эквайринг)"

private const val IS_POS = "(POS-эквайринг)"

private const val IS_PAY = "Оплаченные"

private const val DESCRIPTION_VISA = "Тип операции"

private const val DESCRIPTION = "Комментарий"

private const val DESCRIPTION_PAY = "Назначение платежа"

private const val DATE_SETTLEMENT = "Дата расчетов"

private const val TYPE_TRANSACT =  "Тип"

private const val DIRECTION = "D/C"

private const val DIRECTION_MC = "Направление"

private const val COUNT = "Кол-во"

private const val COUNT_VISA = "Количество"

private const val COUNT_OPER = "Количество операций"

private const val CURRENCY = "Валюта"

private const val ACCOUNT_CURRENCY = "Валюта счета"

private const val SETTLEMENT_CURRENCY = "Валюта расчетов"

private const val OPER_CURRENCY = "Валюта операции"

private const val AMOUNT = "Сумма"

private const val ACCOUNT_AMOUNT = "Сумма в валюте счета"

private const val FOR_SETTLEMENT_AMOUNT = "Сумма для расчетов"

private const val SETTLEMENT_AMOUNT = "Сумма в валюте расчетов"

private const val OPER_AMOUNT = "Сумма в валюте операции"

private const val AMOUNT_RUR = "Сумма в рублях"

private enum class PaySystem(val label: String, val isNspk: Int, val dbValue: String) {
    NONE("", 0, "NONE"),
    MC("MasterCard", 0, "MC"),
    MC_NSPK("MasterCard.НСПК", 1, "MC"),
    VISA("VISA", 0, "VISA"),
    MIR("МИР", 0, "MIR");

    companion object {
        fun paySystemByLabel(label: String): PaySystem {
            for(value in values()) {
                if(value.label == label) {
                    return value
                }
            }

            throw Exception("для label=$label не найдена PaySystem")
        }
    }
}

private enum class TypePayInfo(val dbValue: Int) {
    TransactTotal(9),
    FeePay(0),
    ConverseFromRur(3),
    ConverseToRur(6),
    SettlementPaySystem(1),
    SettlementCorrespondent(2),
    ClaimMessage(8),
    DetailFeeVisa(7),
    PayCalcVisa(4),
    NoCalcOper(4);

    companion object {
        fun typePayInfoByHeaders(header1: String, header2: String, paySystem: String): TypePayInfo {

            if(paySystem.isEmpty()) return typePayInfoIfEmptyPaySystem(header1, header2)

            return when {
                header1.indexOf(CLAIM_MESSAGE) == 0 -> ClaimMessage
                isFeeType(header1, header2) -> FeePay
                isConverseFromRur(header1, header2) -> ConverseFromRur
                isConverseToRur(header1, header2) -> ConverseToRur
                isSettlementCorrespond(header1, header2) -> SettlementCorrespondent
                isSettlementPaySystem(header1, header2) -> SettlementPaySystem
                isDetailFeeVisa(header1, header2) -> DetailFeeVisa
                isPayCalcVisa(header1, header2) -> PayCalcVisa
                isNoCalcOper(header1, header2) -> NoCalcOper
                else -> throw Exception("not found type for header1=$header1 header2=$header2")
            }
        }

        private fun isNoCalcOper(header1: String, header2: String): Boolean {
            return header1.indexOf(NO_SETTLEMENT_OPER) == 0 ||
                    (header2.indexOf(NO_SETTLEMENT_OPER) == 0 && header1.isBlank())
        }

        private fun isPayCalcVisa(header1: String, header2: String): Boolean {
            return header1.indexOf(CALC_PAY_VISA) == 0 ||
                    (header2.indexOf(CALC_PAY_VISA) == 0 && header1.isBlank())
        }

        private fun isDetailFeeVisa(header1: String, header2: String): Boolean {
            return header1.indexOf(DETAIL_VISA_FEE) == 0 ||
                    (header2.indexOf(DETAIL_VISA_FEE) == 0 && header1.isBlank())
        }

        private fun isSettlementPaySystem(header1: String, header2: String): Boolean {
            return (header1.indexOf(SETTLEMENT_PAYSYSTEM) == 0 && header1.indexOf(SETTLEMENT_CORRESPOND) < 0 ) ||
                    (header2.indexOf(SETTLEMENT_PAYSYSTEM) == 0 && header2.indexOf(SETTLEMENT_CORRESPOND) < 0 && header1.isBlank())
        }

        private fun isSettlementCorrespond(header1: String, header2: String): Boolean {
            return header1.indexOf(SETTLEMENT_CORRESPOND) == 0 ||
                    (header2.indexOf(SETTLEMENT_CORRESPOND) == 0 && header1.isBlank())
        }

        private fun isConverseToRur(header1: String, header2: String): Boolean {
            return header1.indexOf(CONVERSE_TO_RUR_HEADER) == 0 ||
                    (header2.indexOf(CONVERSE_TO_RUR_HEADER) == 0 && header1.isBlank())
        }

        private fun isConverseFromRur(header1: String, header2: String): Boolean {
            return header1.indexOf(CONVERSE_HEADER) == 0 ||
                    (header2.indexOf(CONVERSE_HEADER) == 0 && header1.isBlank())
        }

        private fun isFeeType(header1: String, header2: String): Boolean {
            return (header1.indexOf(FEE_MC_H1) == 0 && header2.indexOf(FEE_MC_H2) == 0 ) ||
                   (header1.indexOf(FEE_VISA) == 0 || (header2.indexOf(FEE_VISA) == 0 && header1.isBlank())) ||
                   (header2.indexOf(FEE_MC_H2) == 0 || (header2.indexOf(FEE_MC_H2) == 0 && header1.isBlank()) )
        }

        private fun typePayInfoIfEmptyPaySystem(header1: String, header2: String): TypePayInfo {
            return if(TRANSACT_TOTAL_H1.equals(header1, true) &&
                TRANSACT_TOTAL_H2.equals(header2, true)         ) {

                TransactTotal
            } else {
                throw Exception("paySystem is empty for header1=$header1 and header2=$header2")
            }
        }
    }
}

private const val NO_SETTLEMENT_OPER = "Нерассчитанные операции"

private const val CALC_PAY_VISA = "Эквайринг (VISA)"

private const val DETAIL_VISA_FEE = "Детализация по VISA Fee"

private const val SETTLEMENT_PAYSYSTEM = "Расчеты по "

private const val SETTLEMENT_CORRESPOND = "Расчеты по коррсчетам"

private const val CONVERSE_TO_RUR_HEADER = "Валютный эквивалент возмещения в " //рублях"

private const val CONVERSE_HEADER = "Рублевый эквивалент возмещения в валюте"

private const val FEE_VISA = "Информация о комиссиях"

private const val FEE_MC_H1 = "ICA = 14500"

private const val FEE_MC_H2 = "Fee Collection и Interchange"

private const val CLAIM_MESSAGE = "Претензионные сообщения"

private const val TRANSACT_TOTAL_H1 = "Общая информация"

private const val TRANSACT_TOTAL_H2 = "Информация о предоставленных в Банк операциях"

private const val INSERT_MAIN = "insert into od.PTKB_CLEARINT(ID, FILE_NAME, ONDATE, DATE_FILENAME) values (?, ?, ?, ?)"

private const val INSERT_PAY =
    "insert into od.PTKB_CLEARINT_PAY(ID, CLEARINT, PAY_SYSTEM, IS_NSPK, HEADER1, HEADER2, TYPE_PAY) values (?, ?, ?, ?, ?, ?, ?)"

private const val INSERT_TRANSACT_TOTAL = """
   insert into od.PTKB_CLEARINT_TRANSACT_TOTAL (ID, CLEARINT, CLEARINT_PAY, SETTLEMENT_DATE, TRANSACT, DIRECTION, 
       COUNT_TRANSACT, ACCOUNT_AMOUNT, ACCOUNT_CURRENCY, SETTLEMENT_AMOUNT, SETTLEMENT_CURRENCY, FOR_SETTLEMENT_AMOUNT, FOR_SETTLEMENT_CURRENCY)
       values (classified.nextval, ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?, ?, ?)"""

private const val INSERT_PAY_INFO = """
    insert into od.PTKB_CLEARINT_PAY_INFO (ID, CLEARINT_PAY, SETTLEMENT_DATE, DIRECTION, SETTLEMENT_AMOUNT, SETTLEMENT_CURRENCY, DESCRIPTION)
    values (classified.nextval, ?, ?, ?, ?, ?, ?)
"""

private const val INSERT_PAY_CONVERSE = """
    insert into od.PTKB_CLEARINT_PAY_CONVERSE (ID, CLEARINT_PAY, SETTLEMENT_DATE, CURRENCY_TO, AMOUNT_TO, CURRENCY_FROM, AMOUNT_FROM)
    values (classified.nextval, ?, ?, ?, ?, ?, ?)
"""

private const val INSERT_PAY_CALC = """
    insert into od.PTKB_CLEARINT_PAY_CALC (ID, CLEARINT_PAY, SETTLEMENT_DATE, IS_PAY, AMOUNT, CURRENCY, COUNT_TRANSACT, DESCRIPTION_CALC, TYPE_CALC)
    values (classified.nextval, ?, ?, ?, ?, ?, ?, ?, ?)
"""

private const val INSERT_PAY_CALC_MC = """
    insert into od.PTKB_CLEARINT_PAY_CALC (ID, IS_PAY, CLEARINT_PAY, SETTLEMENT_DATE, AMOUNT, CURRENCY, COUNT_TRANSACT, TYPE_CALC, DIRECTION)
    values (classified.nextval, 0, ?, ?, ?, ?, ?, ?, ?)
"""

