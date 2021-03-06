package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.rest.RestAccount

class RestResponseData : AbstractRequestResponse() {

    override fun typeInfo(): String = "СПРБНОСТАТ"

    override fun xsdSchema(): String = "/xsd/BOS_300.xsd"

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        initRestAccounts(idFromFns() )
    }

    private lateinit var viewHelpVar: String

    lateinit var restAccountList: List<RestAccount>

    override fun getViewHelp(): String = viewHelpVar

    private fun initRestAccounts(idFromFns: Number) {

        val accounts = AfinaQuery.selectCursor(SELECT_REST_ACCOUNT, arrayOf(idFromFns))

        createRestList(accounts)

        val isNotDeposit = accounts.firstOrNull { it[1] != null &&
                !"Депозитный".equals((it[1] as String).trim(), true) }

        viewHelpVar = isNotDeposit?.let { TypeResponseValue.REST_NO_DEPOSIT.fnsValue }
                ?: TypeResponseValue.REST_DEPOSIT.fnsValue
    }

    private fun createRestList(accounts: List<Array<Any?>>) {
        restAccountList = accounts.map { RestAccount(it[0] as? String,
                it[1]?.let { x -> (x as String).trim().toUpperCase() }, it[2] as? String, it[3] as? Number) }
    }
}

const val SELECT_REST_ACCOUNT =  "{ ? = call od.PTKB_440P.getRestAccounts( ? ) }"

const val SELECT_ABSENT_ACCOUNT = "{ ? = call od.PTKB_440P.getAbsentAccounts( ? ) }"