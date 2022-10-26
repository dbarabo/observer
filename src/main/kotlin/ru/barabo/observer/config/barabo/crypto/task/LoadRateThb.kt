package ru.barabo.observer.config.barabo.crypto.task

import org.jsoup.Jsoup
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

object LoadRateThb : SingleSelector {

    override val select: String = "select dt.classified from doctree dt where dt.doctype = 1000131174 " +
            "and dt.docstate = 1000000034 and trunc(dt.validfromdate) = trunc(sysdate) and rownum = 1"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(7, 0), LocalTime.of(10, 0), Duration.ofSeconds(1))

    override fun name(): String = "Курс THB -загрузка"

    override fun config(): ConfigTask = PlasticOutSide

    override fun execute(elem: Elem): State {

        val thbRate = thbRate()

        AfinaQuery.execute(EXEC_RATE_THB, params = arrayOf(thbRate))

        return State.OK
    }

    private const val EXEC_RATE_THB = "call od.ptkb_auto_kursCb_load(1000131339, ?)"

    private const val USD_THB_SITE = "https://www.calc.ru/forex-THB-RUB.html" //"https://www.vl.ru/dengi" //"https://www.finanz.ru/valyuty/usd-thb" //"https://www.bloomberg.com/quote/USDTHB:CUR"

    fun thbRate() : Double {

       val body = Jsoup.connect(USD_THB_SITE).get().body()

        val find = body.allElements.firstOrNull {it.text().matches("\\d\\.\\d\\d RUB".toRegex()) }
            ?:throw Exception("Not found rate by X.YY RUB template")

        return find.text().substringBefore(" RUB").trim().toDouble()

        //val find = body.allElements.firstOrNull { it.text().matches(".*1 THB = \\d\\.\\d\\d RUB.*".toRegex()) }
        //    ?:throw Exception("Not found rate by XX,YYYY THB template")
        //logger.error(find.text())

       //val find = body.allElements.firstOrNull { it.text().matches("\\d\\d,\\d\\d\\d\\d THB.*".toRegex()) }
       //        ?:throw Exception("Not found rate by XX,YYYY THB template")

       //return find.text().substringBefore("THB").replace(",", ".").trim().toDouble()
    }
}