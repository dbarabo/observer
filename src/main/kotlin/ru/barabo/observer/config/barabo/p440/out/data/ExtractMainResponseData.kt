package ru.barabo.observer.config.barabo.p440.out.data

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.OutType
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.extract.AdditionalExtractXml
import ru.barabo.observer.config.task.p440.out.xml.extract.ExtractMainAccount
import java.util.*


class ExtractMainResponseData :AbstractRequestResponse() {

    private val logger = LoggerFactory.getLogger(ExtractMainResponseData::class.java)!!

    override fun typeInfo(): String = "ВЫПБНОСНОВ"

    override fun xsdSchema(): String = "/xsd/BVS_300.xsd"

    private lateinit var viewHelpVar :String

    val extractMainAccountList = ArrayList<ExtractMainAccount>()

    override fun getViewHelp(): String = viewHelpVar

    var countAddFiles :Int = 0

    companion object {
        private const val INSERT_BVD_RESPONSE = "insert into od.ptkb_440p_response (id, FNS_FROM, IS_PB, STATE, FILE_NAME, SENT) " +
                "values (classified.nextval, ?, ?, 2, ?, sysdate) "

        private const val SELECT_MAIN_EXTRACT_ACCOUNT =  "{ ? = call od.PTKB_440P.getExtractAccounts( ? ) }"

        private const val SELECT_EXTRACT_EXISTS_OPERATION = "{ ? = call od.PTKB_440P.isExistsOperation( ? ) }"

        private const val EXEC_OPERATION_ACCOUNT =  "call od.PTKB_EXPORT_EXTRACT(?, ?, ?, ?, 0)"

        private const val SELECT_EXTRACT = "select p.oper, substr(p.Description, 1, 160), p.SHIFR, p.NUMBER_DOC, " +
                "p.DATE_DOC, p.BANK_ACCOUNT, p.BANK_NAME, p.BANK_BIK, p.PAY_NAME, p.PAY_INN, p.PAY_KPP, " +
                "p.PAY_ACCOUNT, p.SUM_DEB, p.SUM_CRED from od.PTKB_TMP_EXTRACT p where sid = ? order by ORD"
    }

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        val isExistsOperation = initExtractAccounts(idFromFns() /*idResponse*/)

        if(isExistsOperation) {

            try {
                extractMainAccountList.forEach { createAccountFiles(it, sessionSetting) }
            } catch (e :Exception) {
                logger.error("fillDataFields", e)

                AfinaQuery.rollbackFree(sessionSetting)

                throw Exception(e.message)
            }
        }
    }

    private fun createAccountFiles(extractMainAccount :ExtractMainAccount, sessionSetting : SessionSetting) {

        var countOperation = 0

        var partNumber = 1

        do {
            val additionalResponseData = AdditionalResponseData(this, extractMainAccount,
                    extractMainAccountList.indexOf(extractMainAccount) + 1, partNumber, countOperation)

            val addExtractXml = AdditionalExtractXml(additionalResponseData)

            countOperation += addExtractXml.addExtractInfoPart.operationAccountListSize

            // если операций в доп. файле нет и это уже не первая часть
            if(partNumber > 1 && addExtractXml.addExtractInfoPart.operationAccountListSize == 0) break

            GeneralCreator.saveXml(additionalResponseData.fileNameResponse(), addExtractXml)

            GeneralCreator.validateXml(additionalResponseData.fileNameResponse(), additionalResponseData.xsdSchema())

            insertBvdResponseDb(additionalResponseData, sessionSetting)

            partNumber++
        } while (addExtractXml.addExtractInfoPart?.operationAccountListSize?:0 > 0)

        countAddFiles += (partNumber - 1)
    }

    private fun insertBvdResponseDb(addResponseData :AdditionalResponseData, sessionSetting : SessionSetting) {

        AfinaQuery.execute(INSERT_BVD_RESPONSE,
                arrayOf(addResponseData.idFromFns(),
                        OutType.EXTRACT_ADDITIONAL.dbValue, addResponseData.fileNameResponseTemplate()), sessionSetting)
    }

    private fun initExtractAccounts(idFromFns: Number) :Boolean {

        val accounts = AfinaQuery.selectCursor(SELECT_MAIN_EXTRACT_ACCOUNT, arrayOf(idFromFns))

        var isDepositAccountOnly = true

        var index = 1

        extractMainAccountList.clear()

        countAddFiles = 0

        for (row in accounts) {

            val typeAccount = row[1]?.let { (row[1] as String).trim().toUpperCase() }

            if(!"Депозитный".equals(typeAccount, true)) {
                isDepositAccountOnly = false
            }

            val account = row[0] as? String

            val currency = row[2] as? String

            val startExtract = row[3] as? Date

            val endExtract = row[4] as? Date

            val startRest = row[5] as? Number

            val endRest = row[6] as? Number

            val debetAmount = row[7] as? Number

            val creditAmount = row[8] as? Number

            val extractAccount = ExtractMainAccount(index, account, currency, startExtract, endExtract,
                    startRest, endRest, debetAmount, creditAmount)

            extractMainAccountList.add(extractAccount)

            index++
        }

        viewHelpVar = if(isDepositAccountOnly) TypeResponseValue.EXTRACT_DEPOSIT.fnsValue
                        else TypeResponseValue.EXTRACT_NO_DEPOSIT.fnsValue

        return accounts.isNotEmpty() && getExistsOperation(idFromFns)
    }

    private fun getExistsOperation(idFromFns: Number) :Boolean {

        val accounts = AfinaQuery.selectCursor(SELECT_EXTRACT_EXISTS_OPERATION, arrayOf(idFromFns))

        return accounts.isNotEmpty() && (accounts[0][0] as? Number)?.toInt() != 0
    }

    private lateinit var operationData :List<Array<Any?>>

    fun getOperationAccount(account :ExtractMainAccount, operationPosition :Int) :List<Array<Any?>> {

        if(operationPosition == 0) {
            operationData = initOperation(account)
        }

        return operationData.drop(1 + operationPosition)
    }

    private fun initOperation(account :ExtractMainAccount) :List<Array<Any?>> {

        val seq = AfinaQuery.nextSequence()

        val sessionBack = AfinaQuery.uniqueSession()

        val params :Array<Any?> = arrayOf(account.startExtract!!, account.endExtract!!, account.code!!, seq)

        AfinaQuery.execute(EXEC_OPERATION_ACCOUNT, params, sessionBack)

        val values = AfinaQuery.select(SELECT_EXTRACT, arrayOf(seq), sessionBack)

        AfinaQuery.rollbackFree(sessionBack)

        return values
    }

}