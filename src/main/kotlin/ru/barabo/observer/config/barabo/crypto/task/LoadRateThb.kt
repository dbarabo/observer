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

    //private val logger = LoggerFactory.getLogger(LoadRateThb::class.java)

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

    private const val EXEC_RATE_THB = "call od.ptkb_auto_kursCb_load(1000131339, ?)"

    /*
     *  перешли на https с 03.07.2018
     */
    private const val USD_THB_SITE = "https://www.bloomberg.com/quote/USDTHB:CUR"

    private fun thbRate() : Double {

       val item = Jsoup.connect(USD_THB_SITE).get().getElementsByAttributeValue("itemprop", "price")
               ?.firstOrNull { !it.attr("content").trim().isEmpty() }

        //logger.error("item=$item")

        /* старый дизайн сайта был - закончился 2015-06-04
             Element mainHeaderElement = doc.select("span.price").first();
             value = mainHeaderElement.childNode(0).outerHtml().trim();
         */

        return item!!.attr("content").trim().toDouble()
    }
}