package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.task.isNewFormat2021
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.rest.RestAccount
import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountAbsent
import ru.barabo.observer.config.task.p440.out.xml.ver4.RestAccountVer4
import java.util.*

class RestResponseData : AbstractRequestResponse() {

    override fun typeInfo(): String = if(isNewFormat2021() ) "СПРОБОСТАТ" else "СПРБНОСТАТ"

    override fun xsdSchema(): String = "/xsd/BOS_300.xsd"

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        initRestAccounts(idFromFns() )
    }

    private lateinit var viewHelpVar: String

    lateinit var restAccountList: List<RestAccount>

    lateinit var restAccountsVer4: List<RestAccountVer4>

    lateinit var accountAbsents: List<AccountAbsent>

    override fun getViewHelp(): String = viewHelpVar

    private fun initRestAccounts(idFromFns: Number) {

        val accounts = AfinaQuery.selectCursor(SELECT_REST_ACCOUNT, arrayOf(idFromFns))

        createRestList(accounts, idFromFns)

        val isNotDeposit = accounts.firstOrNull { it[1] != null &&
                !"Депозитный".equals((it[1] as String).trim(), true) }

        viewHelpVar = isNotDeposit?.let { TypeResponseValue.REST_NO_DEPOSIT.fnsValue }
                ?: TypeResponseValue.REST_DEPOSIT.fnsValue
    }

    private fun createRestList(accounts: List<Array<Any?>>, idFromFns: Number) {
        if(isNewFormat2021()) {
            restAccountsVer4 = accounts.map {
                RestAccountVer4(it[0] as String,
                    it[4] as? Date,
                    it[5] as? Date,
                    it[6] as? Date,

                    it[2] as String,

                    it[7] as String,
                    it[1] as? String,

                    it[3] as Number,

                    it[8] as Date)
            }

            val absentAccount = AfinaQuery.selectCursor(SELECT_ABSENT_ACCOUNT, arrayOf(idFromFns))

            accountAbsents = absentAccount.map {AccountAbsent(it[0] as String) }

        } else {
            restAccountList = accounts.map { RestAccount(it[0] as? String,
                it[1]?.let { x -> (x as String).trim().toUpperCase() }, it[2] as? String, it[3] as? Number) }
        }
    }
}

private const val SELECT_REST_ACCOUNT =  "{ ? = call od.PTKB_440P.getRestAccounts( ? ) }"

const val SELECT_ABSENT_ACCOUNT = "{ ? = call od.PTKB_440P.getAbsentAccounts( ? ) }"