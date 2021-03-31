package ru.barabo.observer.config.barabo.plastic.turn.checker

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.mail.smtp.BaraboSmtp

object CashOutCountryChecker {

    fun check(idMtl: Number, fileNameMtl: String) {

        val title = title(fileNameMtl)

        val html = createHtmlData(idMtl, title) ?: return

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.CHECKER_COUNTRY, bcc = BaraboSmtp.AUTO,
            subject = title, body = html, subtypeBody = "html")
    }

    private fun createHtmlData(idMtl: Number, title: String): String? {

        val data = AfinaQuery.selectCursor(SELECT_CHECK_MTL, arrayOf(idMtl))

        if(data.isEmpty()) return null

        val content = HtmlContent(title, title, headerTable, data)

        return content.html()
    }

    private fun title(fileNameMtl: String) =
        "Найдены снятия наличных в банкоматах картой иностранного эмитента в файле транзакций $fileNameMtl"
}

private val headerTable = mapOf (
    "№ Карты" to "center",
    "Дата операции" to "right",
    "Сумма снятия (руб)" to "right",
    "Код страны" to "left",
    "Страна эмитента" to "left",
    "№ Банкомата" to "left",
    "Индекс банкомата" to "left",
    "Город банкомата" to "left",
    "Адрес банкомата" to "left",
    "ПС эмитента" to "center"
    )

private const val SELECT_CHECK_MTL = "{ ? = call od.PTKB_PLASTIC_TURN.cashOutByCheckCountryMtl( ? ) }"