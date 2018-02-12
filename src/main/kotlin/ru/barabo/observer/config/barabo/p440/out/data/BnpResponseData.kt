package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur
import java.util.*


class BnpResponseData :AbstractResponseData() {

    override fun typeInfo(): String = "СБЩБННЕИСП"

    override fun xsdSchema(): String = "/xsd/BNP_300.xsd"

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

        checkCodes = rowData[4] as? String

        numberPno = rowData[4] as? String

        datePno = rowData[5] as? Date

        codeFns = rowData[6] as? String

        sumPnoKopeika = rowData[7] as? Number

        payer = PayerJur(rowData[8] as? String, rowData[9] as? String, rowData[10] as? String)

        sumPartExecKopeika = rowData[11] as? Number

        account = rowData[12] as? String
    }
}