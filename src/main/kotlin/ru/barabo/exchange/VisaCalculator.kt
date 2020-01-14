package ru.barabo.exchange

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong

object VisaCalculator {
    /**
     * convert kopieka to usd cents
     */
    fun convertRurToUsd(amountRurKopeika: Long, dateConvert: LocalDateTime): Long {

        val rurVisa = amountRurKopeika.kopeikaToRurFormat()

        val request = siteTemplate(rurVisa, dateConvert.dateToFormatVisa())

        val body = Jsoup.connect(request).get().body()

        return parseVisaResponse(body)
    }

    private fun parseVisaResponse(body: Element): Long {
        val find = body.allElements?.first { it.className()?.indexOf("currency-convertion-result")?:-1 >= 0 }
                ?: throw Exception("currency-convertion-result not found for ${body.text()}")

        val start = find.text().indexOf(RUS_RUBLE)

        val end = find.text().indexOf("United States Dollar")

        if(start in 0 until end) {
            val amountText = find.text().substring(start + RUS_RUBLE.length, end).trim()

            return (amountText.toDouble() * 100).roundToLong()
        } else throw Exception("parseVisaResponse not found RUS_RUBLE in ${body.text()}")
    }

    private const val RUS_RUBLE = "Russian Ruble ="

    private fun siteTemplate(rur: String, dateConvert: String) = "$SITE_HEADER$rur&fee=0.0&exchangedate=$dateConvert$SITE_TAIL"

    private const val SITE_HEADER = "https://usa.visa.com/support/consumer/travel-support/exchange-rate-calculator.html?amount="

    private const val SITE_TAIL = "&fromCurr=USD&toCurr=RUB&submitButton=Calculate+exchange+rate"

    private fun Long.kopeikaToRurFormat(): String = formatAmount.format(this / 100.0)

    private fun LocalDateTime.dateToFormatVisa() = formatDate.format(this)

    private val formatAmount = DecimalFormat("#.##", DecimalFormatSymbols().apply { decimalSeparator = '.' } )

    private val formatDate = DateTimeFormatter.ofPattern("MM%2'F'dd%2'F'yyyy")
}