package ru.barabo.observer.config.skad.plastic.task

import oracle.jdbc.OracleTypes
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.task.LoadRateThb
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.round

object CbrCurrencyLoader : SinglePerpetual {

    override fun name(): String = "Загрузка курсов ЦБ"

    override fun config(): ConfigTask = PlasticOutSide

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 15

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(7, 0), workTimeTo = LocalTime.of(10, 0) )

    override fun execute(elem: Elem): State = execByCheckToday(elem)

    private fun execByCheckToday(elem: Elem, isCheckToday: Boolean = true): State {

        val mainElement = mainElement() ?: return super.execute(elem)

        val dateElement = dateMainElement(mainElement)

        if(isCheckToday && (!dateElement.isEqual(LocalDate.now() )) ) return super.execute(elem)

        return insertCurrency(mainElement, elem, dateElement)
    }

    private fun insertCurrency(mainElement: Element, elem: Elem, dateCurrency: LocalDate): State {

        if(!isExistsCurrencyCheck(dateCurrency)) {
            val currencyId = insertAll(mainElement, java.sql.Date.valueOf(dateCurrency) )

            val afinaRateId = createAfinaCbrRate(currencyId)

            AfinaQuery.execute(EXEC_FIXED_CBR_RATE, params = arrayOf(afinaRateId))
        }

        return nextDayState(elem)
    }

    private fun createAfinaCbrRate(currencyId: Number): Number {

        return AfinaQuery.execute(EXEC_LOAD_CBR_RATE, arrayOf(currencyId),
            outParamTypes = intArrayOf(OracleTypes.NUMBER))?.get(0) as Number
    }

    private fun insertAll(mainElement: Element, dateCurrency: java.sql.Date): Number {
        val sessionSetting = AfinaQuery.uniqueSession()

        val currencyId = AfinaQuery.nextSequence(sessionSetting)

        try {

            val name = mainElement.attr("name") ?: ""

            AfinaQuery.execute(INS_CURRENCY, arrayOf(currencyId, dateCurrency, name), sessionSetting)

            val usdRate = insertDetails(sessionSetting, currencyId, mainElement)

            insertThbRate(currencyId, usdRate, sessionSetting)

        } catch (e :Exception) {

            logger.error("insertAll", e)

            AfinaQuery.rollbackFree(sessionSetting)

            throw Exception(e.message)
        }

        AfinaQuery.commitFree(sessionSetting)

        return currencyId
    }

    private fun insertThbRate(currencyId: Number, usdRurRate: Double, sessionSetting: SessionSetting) {

        val valueThb: Long = try {
            round( usdRurRate * 100000 / LoadRateThb.thbRate() ).toLong()

        } catch (e: Exception) {
            logger.error("insertThbRate", e)

            (AfinaQuery.selectValue(SELECT_LAST_THB_RATE, sessionSetting = sessionSetting) as Number).toLong()
        }

        val params = arrayOf<Any?>(currencyId, "764", "THB", 10, valueThb, 4, "Тайландский Бат")

        AfinaQuery.execute(INS_CURRENCY_DETAIL, params, sessionSetting)
    }

    private fun insertDetails(sessionSetting: SessionSetting, currencyId: Number, mainElement: Element): Double {

        var usdRate: Double? = null

        for(valute in mainElement.children().filter { it.nodeName() == "Valute" } ) {

            val name = valute.children().firstOrNull { it.nodeName() == "Name" }?.text() ?: ""

            val numCode = valute.children().firstOrNull { it.nodeName() == "NumCode" }?.text() ?: ""

            val charCode = valute.children().firstOrNull { it.nodeName() == "CharCode" }?.text() ?: ""

            val nominal = valute.children().firstOrNull { it.nodeName() == "Nominal" }?.text()?.toInt()
                ?: throw Exception("Nominal not found for item $valute")

            val valueText = valute.children().firstOrNull { it.nodeName() == "Value" }?.text()
                ?: throw Exception("Value not found for item $valute")

            val indexSepar = valueText.indexOf(',')

            val value = valueText.replace(",", "").toLong()

            val fraction = valueText.length - indexSepar - 1

            val params = arrayOf<Any?>(currencyId, numCode, charCode, nominal, value, fraction, name)

            AfinaQuery.execute(INS_CURRENCY_DETAIL, params, sessionSetting)

            if(numCode == "840") {
                usdRate = valueText.replace(",", ".").toDouble()
            }
        }

        return usdRate!!
    }

    private fun nextDayState(elem: Elem): State {

        elem.executed = LocalDate.now()
            .plusDays(1)
            .atStartOfDay()
            .plusHours( accessibleData.workTimeFrom.hour.toLong() )

        return State.NONE
    }

    private fun isExistsCurrencyCheck(dateCurrency: LocalDate): Boolean =
        AfinaQuery.selectValue(SELECT_ISEXISTS_CURRENCY, arrayOf( java.sql.Date.valueOf(dateCurrency) )) != null

    private fun dateMainElement(mainElement: Element): LocalDate {

        val dateAttribute = mainElement.attr("Date")?.takeIf { it.isNotBlank() }
            ?: throw Exception("Date of main element must not be empty")

        return LocalDate.parse(dateAttribute,FORMATTER_ELEM_DATE)
    }

    private fun mainElement(date: LocalDate = LocalDate.now()): Element? {

        val xml = try {

            Jsoup.connect( site(date) ).get()

        } catch (e: java.lang.Exception) {

            logger.error("Jsoup.connect", e)

            return null
        }

        return xml?.allElements?.first { it.nodeName() == "ValCurs" }
            ?: throw Exception("ValCurs element not found for date = $date")
    }

    private fun site(date: LocalDate) = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=${FORMATTER.format(date)}"
}

private const val INS_CURRENCY = "insert into OD.PTKB_CURRENCY(ID, CURRENCY_DATE, NAME) values (?, ?, ?)"

private const val EXEC_LOAD_CBR_RATE = "{ call OD.PTKB_PRECEPT.loadExecCbrRate(?, ?) }"

private const val EXEC_FIXED_CBR_RATE = "{ call od.PTKB_PRECEPT.execCbrExchange( ? ) }"

private const val SELECT_LAST_THB_RATE = """
    select d.value_min
      from od.ptkb_currency_detail d
     where d.number_code = '764'
  order by d.id_currency desc"""

private const val INS_CURRENCY_DETAIL = """
    insert into OD.PTKB_CURRENCY_DETAIL(ID, ID_CURRENCY, NUMBER_CODE, CHAR_CODE, NOMINAL, VALUE_MIN, FRACTION_COUNT, NAME) 
    values (classified.nextval, ?, ?, ?, ?, ?, ?, ?)"""

private const val SELECT_ISEXISTS_CURRENCY = "select ID from od.PTKB_CURRENCY where CURRENCY_DATE = ?"

private val logger = LoggerFactory.getLogger(CbrCurrencyLoader::class.java)

private val FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

private val FORMATTER_ELEM_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy")
