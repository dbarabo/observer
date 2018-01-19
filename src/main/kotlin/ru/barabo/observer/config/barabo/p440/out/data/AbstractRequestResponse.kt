package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.barabo.p440.out.RequestResponseData
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerType
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml
import java.util.*

abstract class AbstractRequestResponse :AbstractResponseData(), RequestResponseData {

    override fun addSeparFields(): String = ", f.MAIN_NUMBER, f.MAIN_DATE, od.ptkb_440p.getPbFile(f.id), " +
            "f.FNS_CODEID, f.FNS_NAME, p.TYPE, p.ID_CLIENT, p.NAME, p.INN, p.KPP, p.FIRST_NAME, " +
            "p.LAST_NAME, p.SECOND_NAME, p.ADDRESS, p.BIRHDAY, p.BIRTHPLACE, p.CODE_DOC, p.LINE_NUMBER_DOC, p.DATE_DOC "

    override fun addSeparTables(): String = ", od.PTKB_440P_CLIENT p"

    override fun addWhere(): String = " and p.FNS_FROM = f.id"

    override fun getNumberRequest(): String = numberRequestVar

    override fun getDateRequest(): Date = dateRequestVar

    override fun getPbFileName(): String = pbFileNameVar

    override fun getPayer(): PayerXml = payerVar

    override fun getFns(): FnsXml = fnsVar

    private lateinit var numberRequestVar: String

    private lateinit var dateRequestVar: Date

    private lateinit var pbFileNameVar: String

    private lateinit var fnsVar: FnsXml

    private lateinit var payerVar: PayerXml

    override fun fillDataFields(idResponse :Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        numberRequestVar = rowData[4] as String

        dateRequestVar = rowData[5] as Date

        pbFileNameVar = rowData[6] as String

        // [7, 8] -фнс код и назв.
        fnsVar = FnsXml(rowData[7] as? String, rowData[8] as? String)

        payerVar = createPayer(rowData)
    }

    private fun createPayer(row: Array<Any?>): PayerXml {

        // [9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22]

        /* p.TYPE, p.ID_CLIENT, p.NAME, p.INN, p.KPP, p.FIRST_NAME, p.LAST_NAME,
		 * p.SECOND_NAME, " +
		 * "p.ADDRESS, p.BIRHDAY, p.BIRTHPLACE, p.CODE_DOC, p.LINE_NUMBER_DOC, p.DATE_DOC "
		 * + "" + " */

        val payerType = PayerType.getPayerTypeByDbValue((row[9] as Number).toInt())

        val idClient = row[10] as? Number

        val nameJuric = row[11] as? String

        val inn = row[12] as? String

        val kpp = row[13] as? String

        val firstName = row[14] as? String

        val lastName = row[15] as? String

        val secondName = row[16] as? String

        val address = row[17] as? String

        val birhday = row[18] as? Date

        val birhPlace = row[19] as? String

        val codeDoc = row[20] as? String

        val lineNumberDoc = row[21] as? String

        val dateDoc = row[22] as? Date

        when (payerType) {
            PayerType.Juric -> return PayerXml.createJuric(idClient, nameJuric, inn, kpp)

            PayerType.Pboul -> return PayerXml.createPboul(idClient, inn, firstName, lastName, secondName)

            PayerType.Physic -> return PayerXml.createPhysicShort(idClient, inn, firstName, lastName, secondName,
                    address, birhday, birhPlace, codeDoc, lineNumberDoc, dateDoc)

            else -> throw Exception("unknown payerType type $payerType")
        }
    }
}