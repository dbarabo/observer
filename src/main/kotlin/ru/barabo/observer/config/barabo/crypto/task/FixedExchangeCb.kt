package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object FixedExchangeCb : SingleSelector {
    override val select: String = "select dt.classified from doctree dt where dt.doctype = 1000131174 " +
            "and dt.docstate = 1000000034 and trunc(dt.validfromdate) = trunc(sysdate) and rownum = 1"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(7, 0), LocalTime.of(14, 0), Duration.ofSeconds(50))

    // override fun isAccess()  = super.isAccess() && (/*!*/AfinaQuery.isWorkDayNow())

    override fun name(): String = "Фиксирование курса ЦБ"

    override fun config(): ConfigTask = PlasticOutSide // CryptoConfig

    override fun execute(elem: Elem): State {
        AfinaQuery.execute(EXEC_EXCHANGE_CBR, params = arrayOf(elem.idElem))

        return State.OK
    }

    private const val EXEC_EXCHANGE_CBR = "{ call od.PTKB_PRECEPT.execCbrExchange( ? ) }"
}