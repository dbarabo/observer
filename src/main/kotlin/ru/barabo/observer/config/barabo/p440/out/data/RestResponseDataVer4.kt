package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountAbsent
import ru.barabo.observer.config.task.p440.out.xml.ver4.RestAccountVer4
import java.util.*

class RestResponseDataVer4 : AbstractRequestResponse() {
    override fun typeInfo(): String = "СПРОБОСТАТ"

    override fun xsdSchema(): String = "/xsd/440-П_BOS.xsd"

    private lateinit var viewHelpVar: String

    lateinit var restAccountsVer4: List<RestAccountVer4>

    lateinit var accountAbsents: List<AccountAbsent>

    override fun getViewHelp(): String = viewHelpVar

    override fun fillDataFields(idResponse: Number, rowData: Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        initRestAccounts(idFromFns())
    }

    private fun initRestAccounts(idFromFns: Number) {

        val accounts = AfinaQuery.selectCursor(SELECT_REST_ACCOUNT, arrayOf(idFromFns))

        createRestList(accounts, idFromFns)

        val isNotDeposit = accounts.firstOrNull {
            it[1] != null &&
                    !"Депозитный".equals((it[1] as String).trim(), true)
        }

        viewHelpVar = isNotDeposit?.let { TypeResponseValue.REST_NO_DEPOSIT.fnsValue }
            ?: TypeResponseValue.REST_DEPOSIT.fnsValue
    }

    private fun createRestList(accounts: List<Array<Any?>>, idFromFns: Number) {
        restAccountsVer4 = accounts.map {
            RestAccountVer4(
                it[0] as String,
                it[4] as? Date,
                it[5] as? Date,
                it[6] as? Date,

                it[2] as String,

                it[7] as String,
                it[1] as? String,

                it[3] as Number,

                it[8] as Date
            )
        }

        val absentAccount = AfinaQuery.selectCursor(SELECT_ABSENT_ACCOUNT, arrayOf(idFromFns))

        accountAbsents = absentAccount.map { AccountAbsent(it[0] as String) }
    }
}
