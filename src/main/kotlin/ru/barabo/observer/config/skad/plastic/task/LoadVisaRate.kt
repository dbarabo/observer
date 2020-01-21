package ru.barabo.observer.config.skad.plastic.task

import ru.barabo.exchange.VisaCalculator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp
import java.time.*
import java.time.format.DateTimeFormatter

object LoadVisaRate : SingleSelector {

    override fun name(): String = "Загрузка курса VISA"

    override fun config(): ConfigTask = PlasticOutSide

    override val select: String = """
select trunc(ad.onedate + 50 - trunc(sysdate) ), to_char(ad.onedate, 'dd/mm/yyyy')
from od.alldates ad
where ad.onedate > sysdate-15
  and ad.onedate < sysdate
  and not exists (select 1  from od.PTKB_EXCHANGE_RATE_PAY p
     where trunc(period_from) = ad.onedate
       and p.pay_system = 'VISA')
order by ad.onedate"""

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(7, 0), LocalTime.of(8, 30), Duration.ZERO)

    override fun execute(elem: Elem): State {

        val dateRate = elem.name.toDate()

        val timeStamp = Timestamp(dateRate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() )

        val usdCent = VisaCalculator.getExchangeUsd(dateRate) //convertRurToUsd(10000, dateRate)

        AfinaQuery.execute(EXEC_ADD_RATE_VISA, params = arrayOf(timeStamp, 840, usdCent,
                Double::class.javaObjectType, Double::class.javaObjectType, "VISA"))

        return State.OK
    }

    private fun String.toDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    private const val EXEC_ADD_RATE_VISA: String = "{ call od.PTKB_PLASTIC_TURN.addExchangeRate(?, ?, ?, ?, ?, ?) }"
}