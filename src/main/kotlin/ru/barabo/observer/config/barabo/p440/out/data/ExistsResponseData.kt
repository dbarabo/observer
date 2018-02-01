package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.p440.out.xml.TypeResponseValue
import ru.barabo.observer.config.task.p440.out.xml.exists.ExistsAccount
import java.util.*

class ExistsResponseData :AbstractRequestResponse() {

    override fun typeInfo(): String = "СПРБННАЛИЧ"

    override fun xsdSchema(): String = "/xsd/BNS_300.xsd"

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        initExistsAccounts(idResponse)
    }

    companion object {
        private const val SELECT_EXISTS_ACCOUNT = "{ ? = call od.PTKB_440P.getExistsAccounts( ? ) }"
    }

    lateinit var existsAccountList :List<ExistsAccount>

    private lateinit var viewHelpVar :String

    override fun getViewHelp(): String = viewHelpVar

    private fun initExistsAccounts(idResponse: Number) {

        val accounts = AfinaQuery.selectCursor(SELECT_EXISTS_ACCOUNT, arrayOf(idResponse))

        existsAccountList = accounts.map { ExistsAccount(it[0] as? String, it[1]?.let { x -> (x as String).trim().toUpperCase() },
                it[2] as? String, it[3] as? Date, it[4] as? Date) }

        val isNotDeposit = accounts.firstOrNull { it[1] != null &&
                !"Депозитный".equals((it[1] as String).trim(), true) }

        viewHelpVar = isNotDeposit?.let { TypeResponseValue.EXISTS_NO_DEPOSIT.fnsValue }
                ?: TypeResponseValue.EXISTS_DEPOSIT.fnsValue
    }
}