package ru.barabo.observer.config.skad.anywork.task

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.skad.plastic.task.CbrCurrencyLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

object CbrKeyRateLoader : SinglePerpetual {

    override fun name(): String = "Загрузка ключевой ставки ЦБ"

    override fun config(): ConfigTask = AnyWork

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 15

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(8, 0), workTimeTo = LocalTime.of(16, 30) )

    override fun execute(elem: Elem): State {

        val dataElement = dataElement() ?: return super.execute(elem)

        val newData = checkNewData(dataElement) ?: return nextDayState(elem)

        processInfoNewData(newData)

        processForNewRate()

        return nextDayState(elem)
    }

    private fun processForNewRate() {
        AfinaQuery.execute(EXEC_NEW_RATE)
    }

    private fun processInfoNewData(newData: DataRate) {

        val message = """Изменилась ключевая ставка ЦБ
Новая ставка=${formatDecimal2.format(newData.newRate100/100.0)}%
Дата ставки=${newData.newDate}
Значение старой ставки=${formatDecimal2.format(newData.oldRate100/100.0)}
"""
        //BaraboSmtp.sendStubThrows(to = BaraboSmtp.YA, subject = message, body = message )
        BaraboSmtp.sendMantisTicket(message, null)
    }

    private fun nextDayState(elem: Elem): State {

        elem.executed = LocalDate.now()
            .plusDays(1)
            .atStartOfDay()
            .plusHours( CbrCurrencyLoader.accessibleData.workTimeFrom.hour.toLong() )

        return State.NONE
    }

    private fun dataElement(endPeriod: LocalDate = LocalDate.now()): Element? {

        val startPeriod = findMaxDateRate() ?: LocalDate.of(2013, 10, 1)

        val request = getRequest(startPeriod, endPeriod.plusDays(2))

        logger.error(request)

        val body = try {

           Jsoup.connect(request)
                .header("Content-Type","application/x-www-form-urlencoded")
                .cookie("TALanguage", "ALL")
                .data("mode", "filterReviews")
                .data("filterRating", "")
                .data("filterSegment", "")
                .data("filterSeasons", "")
                .data("filterLang", "ALL")
                .referrer(request)
                .header("X-Requested-With", "XMLHttpRequest")
                //.header("X-Puid",xpuid)
                //.data("returnTo",returnTo)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                //.method(Connection.Method.POST)
                .get().body()

        } catch (e: java.lang.Exception) {

            logger.error(request, e)

            return null
        }

        return body.allElements?.first { (it.className()?.indexOf("data"/*"table-wrapper"*/) ?: -1) >= 0 }
            ?: throw Exception("KeyRate element not found for date = $endPeriod")
    }

    private fun getRequest(start: LocalDate, end: LocalDate): String =
        "https://www.cbr.ru/hd_base/KeyRate/?UniDbQuery.Posted=True&UniDbQuery.From=${start.toRusString()}&UniDbQuery.To=${end.toRusString()}"

    private fun findMaxDateRate(): LocalDate? {

        val start = AfinaQuery.selectValue(SELECT_MAX) as? Timestamp ?: return null

        return start.toLocalDateTime().toLocalDate()
    }

    private fun checkNewData(data: Element): DataRate? {

        val maxDate = AfinaQuery.selectValue(SELECT_LAST_DATE)

        var priorRate: Long = if(maxDate == null) 0
        else (AfinaQuery.selectValue(SELECT_LAST_RATE, arrayOf(maxDate)) as Number).toLong()

        val list = cbrList(data)

        var result: DataRate? = null

        for(cbr in list) {
            if(priorRate != cbr.rate100) {
                result = DataRate(cbr.rate100, cbr.date, priorRate)
                addNewRate(result)

                priorRate = cbr.rate100
            }
        }
        return result
    }

    private fun cbrList(data: Element): List<CbrRate> {
        val regex = Pattern.compile("(<td>(.*?)</td>)")

        val regexMatcher = regex.matcher("$data")

        var lastDate: LocalDate = LocalDate.now()

        val listRate = ArrayList<CbrRate>()

        var isDate = true
        while (regexMatcher.find()) {

            val value = regexMatcher.group(2)

            if(isDate) {
                lastDate = value.toLocalDateRus()
            } else {
                val rate = value.replace(",", "").trim().toLong()

                listRate += CbrRate(lastDate, rate)
            }
            isDate = !isDate
        }

        listRate.sortBy { it.date }

        return listRate
    }

    private fun addNewRate(newRate: DataRate) {

        AfinaQuery.execute(INSERT_RATE, arrayOf(newRate.newRate100/100.0, newRate.newDate.toTimestamp()))

        logger.error("newRate=${newRate.newRate100/100.0}")
        logger.error("newDate=${newRate.newDate}")
        logger.error("oldRate=${newRate.oldRate100/100.0}")
    }
}

private val formatDecimal2 = DecimalFormat("##.##", DecimalFormatSymbols().apply { decimalSeparator = ',' } )

private data class CbrRate(val date: LocalDate, val rate100: Long)

private data class DataRate(val newRate100: Long, val newDate: LocalDate, val oldRate100: Long)

fun LocalDate.toRusString(): String = rusPattern.format(this)

fun String.toLocalDateRus(): LocalDate = LocalDate.parse(this, rusPattern)

private val rusPattern = DateTimeFormatter.ofPattern("dd.MM.yyyy")

private const val EXEC_NEW_RATE = "{ call od.PTKB_PRECEPT.addLastKeyRateCbr }"

private const val SELECT_MAX = "select max(BEGAN) + 1 from od.PTKB_CBR_RATE_KEY"

private const val SELECT_LAST_DATE = "select max(BEGAN) from od.PTKB_CBR_RATE_KEY"

private const val SELECT_LAST_RATE = "select RATE*100 from od.PTKB_CBR_RATE_KEY where BEGAN = ?"

private const val INSERT_RATE = "insert into OD.PTKB_CBR_RATE_KEY (ID, RATE, BEGAN) values (classified.nextval, ?, ?)"

private val logger = LoggerFactory.getLogger(CbrKeyRateLoader::class.java)