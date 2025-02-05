package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.ExtractResponseData
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.OutType
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountAbsent
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.ExtractMainAccountVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add.FileAddExtractXmlVer4
import java.util.*
import kotlin.math.min

class ExtractMainResponseDataVer4 : AbstractRequestResponse(), ExtractResponseData {
    override fun typeInfo(): String = "ВЫПБНОСНОВ"

    override fun xsdSchema(): String = "/xsd/440-П_BVS.xsd"

    private lateinit var viewHelpVar: String

    override fun getViewHelp(): String = viewHelpVar

    private lateinit var accountAbsents: List<AccountAbsent>

    private lateinit var mainAccountsVer4: List<ExtractMainAccountVer4>

    override fun getAccountAbsentList(): List<AccountAbsent> = accountAbsents

    override fun getMainAccountList(): List<ExtractMainAccountVer4> = mainAccountsVer4

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        val absentAccount = AfinaQuery.selectCursor(SELECT_ABSENT_ACCOUNT, arrayOf( idFromFns()), sessionSetting )

        accountAbsents = absentAccount.map {AccountAbsent(it[0] as String) }

        createExtractInfo(sessionSetting)
    }

    override fun maxOperationsCountInPart(): Int = MAX_OPERATION_COUNT_EXT_VER4

    private fun createExtractInfo(sessionSetting: SessionSetting) {

        val accounts = AfinaQuery.selectCursor(SELECT_MAIN_EXTRACT_ACCOUNT, arrayOf(idFromFns() ), sessionSetting)

        mainAccountsVer4 = accounts.withIndex().map {
            ExtractMainAccountVer4(
                it.value[0] as String,
                it.value[9] as? Date,
                it.value[11] as? Date,
                it.value[10] as? Date,
                it.index + 1,

                it.value[3] as? Date,
                it.value[4] as? Date,

                it.value[2] as String,

                it.value[12] as? String,
                it.value[1] as? String,

                it.value[5] as? Number,
                it.value[6] as? Number,
                it.value[7] as? Number,
                it.value[8] as? Number,

                (it.value[13] as Number).toInt() == 0
            )
        }

        AfinaQuery.commitFree(sessionSetting)

        val isNotDeposit = mainAccountsVer4.firstOrNull { it.codeTypeAccount != "1200" && it.codeTypeAccount != "1300"}

        viewHelpVar = if(isNotDeposit != null) TypeResponseValue.EXTRACT_NO_DEPOSIT.fnsValue
        else TypeResponseValue.EXTRACT_DEPOSIT.fnsValue

        val accountsWithTurn = mainAccountsVer4.filter { it.groupInfoAddFile == null }

        for(account in accountsWithTurn) {
            val operationDataAccount = initOperationAccount(account)

            val countFiles = countFiles(operationDataAccount)

            account.addGroupInfoAddFile( getAddFileNames(account, countFiles) )

            createAddXmlFiles(account, operationDataAccount)
        }
    }

    private fun createAddXmlFiles(account: ExtractMainAccountVer4, operationDataAccount: List<Array<Any?>>) {

        var countOperation = 0

        val countFiles: Int = countFiles(operationDataAccount)

        for(partNumber in 1..countFiles) {
            val addResponseData =
                AddExtractResponseDataVer4(this, account, partNumber, countOperation, operationDataAccount)

            countOperation += min(maxOperationsCountInPart(), operationDataAccount.size - countOperation - 1)

            val addExtractXml = FileAddExtractXmlVer4(addResponseData)

            val fileXml = GeneralCreator.saveXml(addResponseData.fileNameResponse(), addExtractXml)

            GeneralCreator.validateXml(fileXml, addResponseData.xsdSchema())

            insertBvdResponseDb(addResponseData)
        }
    }

    private fun insertBvdResponseDb(addResponseData: AddExtractResponseDataVer4) {
        AfinaQuery.execute(INSERT_BVD_RESPONSE,
            arrayOf(addResponseData.idFromFns(),
                OutType.EXTRACT_ADDITIONAL.dbValue, addResponseData.fileNameResponseTemplate()) )
    }
}

fun ExtractResponseData.countFiles(operations: List<*>): Int =   (operations.size - 1) / maxOperationsCountInPart() +
        if( ((operations.size - 1) % maxOperationsCountInPart() ) == 0) 0 else 1

fun ResponseData.getAddFileNames(account: ExtractMainAccountVer4, countFiles: Int): List<String> {

    val fileNames = ArrayList<String>()

    val template = this.fileNameResponse().substringBefore(".").replace("BVS", "BVD")

    for(index in 1..countFiles) {

        val fileName = "${template}_${account.orderFile}_${String.format("%06d", index)}"

        fileNames.add(fileName)
    }

    return fileNames
}

fun initOperationAccount(account: ExtractMainAccountVer4): List<Array<Any?>> {

    val seq = AfinaQuery.nextSequence()

    val sessionBack = AfinaQuery.uniqueSession()

    val params: Array<Any?> = arrayOf(account.startExtract, account.endExtract, account.code, seq)

    AfinaQuery.execute(EXEC_OPERATION_ACCOUNT, params, sessionBack)

    val operationDataAccount = AfinaQuery.select(SELECT_EXTRACT, arrayOf(seq), sessionBack)

    AfinaQuery.rollbackFree(sessionBack)

    return operationDataAccount
}

//private val logger = LoggerFactory.getLogger(ExtractMainResponseDataVer4::class.java)

private const val MAX_OPERATION_COUNT_EXT_VER4 = 1000

private const val SELECT_MAIN_EXTRACT_ACCOUNT = "{ ? = call od.PTKB_440P.getExtractMainAccountsVer4( ? ) }"

private const val EXEC_OPERATION_ACCOUNT =  "call od.PTKB_EXPORT_EXTRACT(?, ?, ?, ?, 0)"

private const val SELECT_EXTRACT = "select p.oper, substr(p.Description, 1, 160), p.SHIFR, p.NUMBER_DOC, " +
        "p.DATE_DOC, p.BANK_ACCOUNT, p.BANK_NAME, p.BANK_BIK, p.PAY_NAME, p.PAY_INN, p.PAY_KPP, " +
        "p.PAY_ACCOUNT, p.SUM_DEB, p.SUM_CRED from od.PTKB_TMP_EXTRACT p where sid = ? order by ORD"

private const val INSERT_BVD_RESPONSE = """
    insert into od.ptkb_440p_response (id, FNS_FROM, IS_PB, STATE, FILE_NAME, SENT) 
     values (classified.nextval, ?, ?, 2, ?, sysdate) """