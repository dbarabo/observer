package ru.barabo.observer.config.fns.cbr.extract

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.ExtractResponseData
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator.Companion.validateXml
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import ru.barabo.observer.config.barabo.p440.out.data.*
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import ru.barabo.observer.config.fns.cbr.task.getCbrResponseFolderByRequest
import ru.barabo.observer.config.skad.plastic.task.saveXml
import ru.barabo.observer.config.task.p440.load.xml.impl.Address
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerType
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountAbsent
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.ExtractMainAccountVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add.FileAddExtractXmlVer4
import java.io.File
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min

class ExtractMainResponseCbr : ExtractResponseData {

    private lateinit var idListCbr: Number

    private lateinit var _fileNameResponseTemplate: String

    private lateinit var _fileNameResponse: String

    private lateinit var _fileNameFromFns: String

    override fun typeInfo(): String = "ВЫПБНОСНОВ"

    override fun xsdSchema(): String = "/xsd/cbr/BVS.xsd"

    override fun isSourceSmev(): Boolean = false

    override fun fileNameResponse(): String = _fileNameResponse

    override fun fileNameFromFns(): String = _fileNameFromFns

    override fun fileNameResponseTemplate(): String = _fileNameResponseTemplate

    override fun idFromFns(): Number = idListCbr

    private lateinit var _requestIdCbr: String

    private lateinit var _onStateDate: Date

    private lateinit var _endPeriod: Date

    private lateinit var _pbFileNameVar: String

    private lateinit var _payerVar: PayerXml

    private lateinit var _accountAbsentList: List<AccountAbsent>

    private lateinit var _mainAccountsVer4List: List<ExtractMainAccountVer4>

    override fun getAccountAbsentList(): List<AccountAbsent> = _accountAbsentList

    override fun getMainAccountList(): List<ExtractMainAccountVer4> = _mainAccountsVer4List

    override fun maxOperationsCountInPart(): Int = MAX_OPERATION_COUNT_CBR

    override fun getNumberRequest(): String = _requestIdCbr.substring(12).trimStart('0')

    override fun getDateRequest(): Date = _requestIdCbr.substring(4..11).noSeparatorToTimestamp()

    override fun getIdRequest(): String = _requestIdCbr

    override fun getFns(): FnsXml = FnsXml(_requestIdCbr.substring(0..3), "")

    override fun getOnStateDateRequest(): Date? = null

    override fun getStartPeriodRequest(): Date = _onStateDate

    override fun getEndPeriodRequest(): Date = _endPeriod

    override fun getPbFileName(): String = _pbFileNameVar

    override fun getPayer(): PayerXml = _payerVar

    private lateinit var _viewHelpVar: String

    override fun getViewHelp(): String = _viewHelpVar

    override fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData {

        initResponseData(idResponse, sessionSetting)

        initRequestData(idResponse, sessionSetting)

        _payerVar = getPayerInfo(idResponse, sessionSetting)

        _accountAbsentList = initAbsentAccounts(sessionSetting)

        _mainAccountsVer4List = initMainAccounts(sessionSetting)

        return this
    }

    private fun initMainAccounts(sessionSetting: SessionSetting): List<ExtractMainAccountVer4> {

        val accounts = AfinaQuery.selectCursor(SELECT_MAIN_EXTRACT_ACCOUNT, arrayOf(idListCbr), sessionSetting)

        _mainAccountsVer4List = accounts.withIndex().map {
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

        val isNotDeposit = _mainAccountsVer4List.firstOrNull { it.codeTypeAccount != "1200" && it.codeTypeAccount != "1300"}

        _viewHelpVar = if(isNotDeposit != null) TypeResponseValue.EXTRACT_NO_DEPOSIT.fnsValue
                        else TypeResponseValue.EXTRACT_DEPOSIT.fnsValue

        val accountsWithTurn = _mainAccountsVer4List.filter { it.groupInfoAddFile == null }

        for(account in accountsWithTurn) {
            val operationDataAccount = initOperationAccount(account)

            val countFiles = countFiles(operationDataAccount)

            account.addGroupInfoAddFile( getAddFileNames(account, countFiles) )

            createAddXmlFiles(account, operationDataAccount)
        }

        return _mainAccountsVer4List
    }

    private fun createAddXmlFiles(account: ExtractMainAccountVer4, operationDataAccount: List<Array<Any?>>) {

        var countOperation = 0

        val countFiles: Int = countFiles(operationDataAccount)

        for(partNumber in 1..countFiles) {
            val addResponseData = AddExtractResponseDataVer4(this, account, partNumber, countOperation, operationDataAccount)

            countOperation += min(maxOperationsCountInPart(), operationDataAccount.size - countOperation - 1)

            val addExtractXml = FileAddExtractXmlVer4(addResponseData)

            val file = File("${getCbrResponseFolderByRequest(fileNameFromFns()).absolutePath}/${addResponseData.fileNameResponse()}")

            saveXml(file, addExtractXml, isUseAttr = true)

            validateXml(file, addResponseData.xsdSchema())

            insertBvdResponseDb(addResponseData)
        }
    }

    private fun insertBvdResponseDb(addResponseData: AddExtractResponseDataVer4) {
        AfinaQuery.execute(INSERT_BVD_RESPONSE,
            arrayOf(addResponseData.idFromFns(), addResponseData.fileNameResponseTemplate()) )
    }

    private fun initAbsentAccounts(sessionSetting: SessionSetting): List<AccountAbsent> {

        return AfinaQuery.selectCursor(SELECT_ABSENT_ACCOUNT, arrayOf(idListCbr), sessionSetting)
            .map { AccountAbsent(it[0] as String) }
    }

    private fun getPayerInfo(idResponse: Number, sessionSetting: SessionSetting): PayerXml {

        val clientInfo = AfinaQuery.selectCursor(SELECT_PAYER_INFO, arrayOf(idResponse), sessionSetting)[0]

        val typeClient = PayerType.getPayerTypeByDbValue((clientInfo[0] as Number).toInt())

        val clientId = clientInfo[1] as Number

        val name = clientInfo[2] as String

        val inn = clientInfo[3] as? String

        val kpp = clientInfo[4] as? String

        val firstName = clientInfo[5] as? String

        val lastName = clientInfo[6] as? String

        val secondName = clientInfo[7] as? String

        val birthDay = clientInfo[8] as? Date

        val birthPlace = clientInfo[9] as? String

        val codeDoc = clientInfo[10] as? String

        val lineNumberDoc = (clientInfo[11] as? String) ?: ""

        val dateDoc = clientInfo[12] as? Date

        return when (typeClient) {
            PayerType.Juric -> PayerXml.createJuric(clientId, name, inn, kpp)

            PayerType.Pboul -> PayerXml.createPboul(clientId, inn, firstName, lastName, secondName)

            PayerType.Physic -> createPhysic(sessionSetting, clientId, inn, firstName, lastName, secondName,
                birthDay, birthPlace, codeDoc, lineNumberDoc, dateDoc)

            else -> throw Exception("unknown payerType type $typeClient")
        }
    }

    private fun createPhysic(sessionSetting: SessionSetting,
              clientId: Number, inn: String?, firstName: String?, lastName: String?, secondName: String?,
              birthDay: Date?, birthPlace: String?, codeDoc: String?, lineNumberDoc: String, dateDoc: Date?): PayerXml {

        return PayerXml.createPhysic(clientId, inn, firstName, lastName, secondName,null,
            birthDay, birthPlace, codeDoc, lineNumberDoc, dateDoc
        ).apply {

            val addressRow = AfinaQuery.selectCursor(CURSOR_ADDRESS_PHYSIC, arrayOf(clientId), sessionSetting)[0]

            payerPhysic.address = Address.createAddress(
                addressRow[0] as? String,
                addressRow[1] as? String,
                addressRow[2] as? String,
                addressRow[3] as? String,
                addressRow[4] as? String,
                addressRow[5] as? String,
                addressRow[6] as? String,
                addressRow[7] as? String,
                addressRow[8] as? String
            )
        }
    }

    private fun initRequestData(idResponse: Number, sessionSetting: SessionSetting) {
        val (requestIdCbr, startDate, endDate, pbFile) =
            AfinaQuery.selectCursor(SELECT_REQUEST_DATA, arrayOf(idResponse), sessionSetting)[0]

        _requestIdCbr = requestIdCbr as String

        _onStateDate =  startDate as Timestamp

        _endPeriod = endDate as Timestamp

        _pbFileNameVar = pbFile as String
    }

    private fun initResponseData(idResponse: Number, sessionSetting: SessionSetting) {
        val (idMain, fileTemplate, fileRequest) =
            AfinaQuery.selectCursor(SELECT_MAIN_CBR, arrayOf(idResponse), sessionSetting)[0]

        idListCbr = idMain as Number

        _fileNameResponseTemplate = (fileTemplate as String).substringBeforeLast(".")

        _fileNameResponse = String.format(_fileNameResponseTemplate, dateFormatInFile())

        _fileNameFromFns =  fileRequest as String
    }
}

private const val MAX_OPERATION_COUNT_CBR = 2_000_000_000

fun String.noSeparatorToLocalDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyyMMdd"))

fun String.noSeparatorToTimestamp(): Timestamp = noSeparatorToLocalDate().toTimestamp()

private const val INSERT_BVD_RESPONSE = """
    insert into od.PTKB_CBR_EXT_RESPONSE (id, ID_LIST, TYPE_RESPONSE, FILENAME, SENT)
     values (classified.nextval, ?, 3, ?, sysdate) """

private const val SELECT_MAIN_CBR = "{ ? = call od.PTKB_CBR_EXTRACT.getMainRecordByResponseId( ? ) }"

private const val SELECT_REQUEST_DATA = "{ ? = call od.PTKB_CBR_EXTRACT.getMainRequestData( ? ) }"

private const val SELECT_PAYER_INFO = "{ ? = call od.PTKB_CBR_EXTRACT.getPayerInfo( ? ) }"

private const val SELECT_ABSENT_ACCOUNT = "{ ? = call od.PTKB_CBR_EXTRACT.getAbsentAccounts( ? ) }"

private const val SELECT_MAIN_EXTRACT_ACCOUNT = "{ ? = call od.PTKB_CBR_EXTRACT.getExtractMainAccounts( ? ) }"