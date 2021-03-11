package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.ver4.exists.ExistsAccountVer4
import java.util.*

class ExistsResponseDataVer4 : AbstractRequestResponse() {

    override fun typeInfo(): String = "СПРБННАЛИЧ"

    override fun xsdSchema(): String = "/xsd/440-П_BNS.xsd"

    private lateinit var viewHelpVar: String

    lateinit var existsAccountsVer4: List<ExistsAccountVer4>

    override fun getOnStateDateRequest(): Date?  =
        super.getOnStateDateRequest() ?: if(getStartPeriodRequest() != null) null else Date()

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        initExistsAccounts(idFromFns())
    }

    override fun getViewHelp(): String = viewHelpVar

    private fun initExistsAccounts(idFromFns: Number) {

        val accounts = AfinaQuery.selectCursor(SELECT_EXISTS_ACCOUNT, arrayOf(idFromFns))

        createExistsList(accounts)

        val isNotDeposit = accounts.firstOrNull { it[1] != null &&
                !"Депозитный".equals((it[1] as String).trim(), true) }

        viewHelpVar = isNotDeposit?.let { TypeResponseValue.EXISTS_NO_DEPOSIT.fnsValue }
            ?: TypeResponseValue.EXISTS_DEPOSIT.fnsValue
    }

    private fun createExistsList(accounts: List<Array<Any?>>) {
        existsAccountsVer4 = accounts.map {
            ExistsAccountVer4(it[0] as String,
                    it[3] as? Date,
                    it[4] as? Date,
                    it[5] as? Date,

                    it[2] as String,

                    it[6] as String,
                    it[1] as? String)
            }
    }
}