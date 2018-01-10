package ru.barabo.observer.config.barabo.crypto.task

import org.jsoup.Jsoup
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
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

    override fun config(): ConfigTask = CryptoConfig

    override fun execute(elem: Elem): State {

        val thbRate = thbRate()

        AfinaQuery.execute(EXEC_RATE_THB, params = arrayOf(thbRate))

        return State.OK
    }

    private val EXEC_RATE_THB = "call od.ptkb_auto_kursCb_load(1000131339, ?)"

    private val USD_THB_SITE = "http://www.bloomberg.com/quote/USDthb:cur"

    private fun thbRate() : Double {


       val item = Jsoup.connect(USD_THB_SITE).get().getElementsByAttributeValue("itemprop", "price")
               ?.firstOrNull { !it.attr("content").trim().isEmpty() }

        /* старый дизайн сайта был - закончился 2015-06-04
             Element mainHeaderElement = doc.select("span.price").first();
             value = mainHeaderElement.childNode(0).outerHtml().trim();
         */

        return item!!.attr("content").trim().toDouble()
    }
}