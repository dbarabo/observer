package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.barabo.p440.out.RequestResponseData
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerType
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml
import java.text.SimpleDateFormat
import java.util.*

private val dateFormatter = SimpleDateFormat("yyyyMMdd")

abstract class AbstractRequestResponse : AbstractResponseData(), RequestResponseData {

    override fun addSeparFields(): String = ", f.MAIN_NUMBER, f.MAIN_DATE, od.ptkb_440p.getPbFile(f.id), " +
            "f.FNS_CODEID, f.FNS_NAME, f.EXTANDED_DATE, f.EXT_END_PERIOD, p.TYPE, p.ID_CLIENT, p.NAME, p.INN, p.KPP, p.FIRST_NAME, " +
            "p.LAST_NAME, p.SECOND_NAME, p.ADDRESS, p.BIRHDAY, p.BIRTHPLACE, p.CODE_DOC, p.LINE_NUMBER_DOC, p.DATE_DOC "

    override fun addSeparTables(): String = ", od.PTKB_440P_CLIENT p"

    override fun addWhere(): String = " and p.FNS_FROM = f.id"

    override fun getNumberRequest(): String = numberRequestVar

    override fun getDateRequest(): Date = dateRequestVar

    override fun getOnStateDateRequest(): Date? = if(endPeriod != null) null else onStateDate

    override fun getStartPeriodRequest(): Date? = if(endPeriod == null) null else onStateDate

    override fun getEndPeriodRequest(): Date? = endPeriod

    override fun getPbFileName(): String = pbFileNameVar

    override fun getPayer(): PayerXml = payerVar

    override fun getFns(): FnsXml = fnsVar

    private lateinit var numberRequestVar: String

    private lateinit var dateRequestVar: Date

    private var onStateDate: Date? = null

    private var endPeriod: Date? = null

    private lateinit var pbFileNameVar: String

    private lateinit var fnsVar: FnsXml

    private lateinit var payerVar: PayerXml

    fun getIdRequest(): String =
        "${fnsVar.fnsCode}${dateFormatter.format(getDateRequest())}${getNumberRequest().padStart(10, '0')}"

    override fun fillDataFields(idResponse :Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        numberRequestVar = rowData[4] as String

        dateRequestVar = rowData[5] as Date

        pbFileNameVar = rowData[6] as String

        // [7, 8] -фнс код и назв.
        fnsVar = FnsXml(rowData[7] as? String, rowData[8] as? String)

        onStateDate = rowData[9] as? Date

        endPeriod = rowData[10] as? Date

        payerVar = createPayer(rowData, 11)
    }

    private fun createPayer(row: Array<Any?>, indexPayer: Int): PayerXml {

        // [9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22]

        /* p.TYPE, p.ID_CLIENT, p.NAME, p.INN, p.KPP, p.FIRST_NAME, p.LAST_NAME,
		 * p.SECOND_NAME, " +
		 * "p.ADDRESS, p.BIRHDAY, p.BIRTHPLACE, p.CODE_DOC, p.LINE_NUMBER_DOC, p.DATE_DOC "
		 * + "" + " */

        var index: Int = indexPayer
        val payerType = PayerType.getPayerTypeByDbValue((row[index] as Number).toInt())

        index++
        val idClient = row[index] as? Number

        index++
        val nameJuric = row[index] as? String

        index++
        val inn = row[index] as? String

        index++
        val kpp = row[index] as? String

        index++
        val firstName = row[index] as? String

        index++
        val lastName = row[index] as? String

        index++
        val secondName = row[index] as? String

        index++
        val address = row[index] as? String

        index++
        val birhday = row[index] as? Date

        index++
        val birhPlace = row[index] as? String

        index++
        val codeDoc = row[index] as? String

        index++
        val lineNumberDoc = row[index] as? String

        index++
        val dateDoc = row[index] as? Date

        return when (payerType) {
            PayerType.Juric -> PayerXml.createJuric(idClient, nameJuric, inn, kpp)

            PayerType.Pboul -> PayerXml.createPboul(idClient, inn, firstName, lastName, secondName)

            PayerType.Physic -> PayerXml.createPhysicShort(idClient, inn, firstName, lastName, secondName,
                address, birhday, birhPlace, codeDoc, lineNumberDoc, dateDoc)

            else -> throw Exception("unknown payerType type $payerType")
        }
    }
}