package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur
import java.util.*

class BnpResponseDataVer4 : AbstractResponseData() {

    override fun typeInfo(): String = "СБЩБННЕИСП"

    override fun xsdSchema(): String = "/xsd/440-П_BNP.xsd"

    override fun addSeparFields(): String = ", f.MAIN_NUMBER, f.MAIN_DATE, f.FNS_CODEID, f.MAIN_SUM, " +
            "p.INN, p.KPP, p.NAME, od.ptkb_440p.getPnoPartSumExecute(f.id), f.ACCOUNTS "

    override fun addSeparTables(): String = ", od.PTKB_440P_CLIENT p "

    override fun addWhere(): String = " and p.FNS_FROM = f.id "

    private var checkCodes: String? = null

    var numberPno: String? = null

    var datePno: Date? = null

    var codeFns: String? = null

    var sumPnoKopeika: Number? = null

    var sumPartExecKopeika: Number? = null

    var account: String? = null

    lateinit var payer: PayerJur

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        checkCodes = rowData[5] as? String

        numberPno = rowData[5] as? String

        datePno = rowData[6] as? Date

        codeFns = rowData[7] as? String

        sumPnoKopeika = rowData[8] as? Number

        payer = PayerJur(rowData[9] as? String, rowData[10] as? String, rowData[11] as? String)

        sumPartExecKopeika = rowData[12] as? Number

        account = rowData[13] as? String
    }
}