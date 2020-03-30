package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object OutRestCheck: SingleSelector {

    //private val logger = LoggerFactory.getLogger(OutRestCheck::class.java)

    override val select: String = "select id, file_name from od.ptkb_plastic_acc where trunc(created) = trunc(sysdate)"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "Сверка остатков с ПЦ"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        if(!isCtlExecAllDocumentFound()) {
            return if(isEndTime() ) processNoneExecAllDocument() else waitToNextTime(elem)
        }

        return processExecAllDocument()
    }

    private fun isCtlExecAllDocumentFound(): Boolean {
        val ctlDocument = StoreSimple.findFirstByConditionName(task = ExecuteCtlMtl, isConditionName = ::isContainsName)

        return ctlDocument?.let { isAllExecDoc(it.idElem) } ?: false
    }

    private fun isAllExecDoc(idElem: Long?): Boolean {
        val existsNoneDoc = AfinaQuery.selectValue(SELECT_IS_EXISTS_NONE_EXEC_DOC, arrayOf(idElem)) as Number?

        return existsNoneDoc?.let { it.toInt() == 0 } ?: false
    }

    private const val SELECT_IS_EXISTS_NONE_EXEC_DOC = "select od.PTKB_PLASTIC_TURN.isExistsNoneExecCtlDoc(?) from dual"

    private fun isEndTime() = LocalTime.now().hour >= 23

    private fun waitToNextTime(elem: Elem): State {
        elem.executed = LocalDateTime.now().plusMinutes(NEXT_TIME_MINUTE)

        return State.NONE
    }

    private const val NEXT_TIME_MINUTE = 15L

    private fun processNoneExecAllDocument(): State {

        val data = createHtmlData() ?: return State.OK

        sendMailFile(NONE_EXEC_SUBJECT, data)

        return State.OK
    }

    private fun processExecAllDocument(): State {

        val data = createHtmlData() ?: return State.OK

        sendMailFile(EXEC_SUBJECT, data)


        return State.OK
    }

    private fun sendMailFile(subject: String, data: String) {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.DELB_PLASTIC, cc = BaraboSmtp.DOPIKI, bcc = BaraboSmtp.AUTO, subject = subject,
                body = data, subtypeBody = "html") //, attachments = arrayOf(fileSend))
    }

    private const val NONE_EXEC_SUBJECT = "✖✖✖ Сверка остатков из Картстандарта - есть неисполненные док-ты"

    private const val EXEC_SUBJECT = "Сверка остатков Картстандарта и Афины"

    private fun createHtmlData(): String? {

        val data = AfinaQuery.selectCursor(SELECT_REVIEW)

        if(data.isEmpty()) return null

        val checkLimitData = checkLimit(data)

        if(checkLimitData.isEmpty()) return null

        val content = HtmlContent(titleHtml(), titleHtml(), HEADER_TABLE, checkLimitData)

        return content.html()
    }

    private fun checkLimit(data: List<Array<Any?>>): List<Array<Any?>> {

        val checkData = ArrayList<Array<Any?>>()

        for(row in data) {

            val limitOrError = AfinaQuery.selectValue(SELECT_LIMIT_ACCOUNT,  arrayOf(row[0])) as? String

            val limit = limitOrError?.trim()?.toIntOrNull()?.toDouble()?.let { it / 100 }

            val deltaIndex = HEADER_TABLE.keys.indexOf("Сумма несовпадения")

            var newDelta = (row[deltaIndex] as? Number)?.toDouble()?:0.0

            newDelta -= limit?:0.0

            if((newDelta*100.0).toInt() == 0) continue

            val indexLimit = HEADER_TABLE.keys.indexOf("Лимит по счету")

            row[deltaIndex] = NumberFormat.getCurrencyInstance().format(newDelta).substringBeforeLast("руб.")

            row[indexLimit] = limit?.let { NumberFormat.getCurrencyInstance().format(it).substringBeforeLast("руб.") } ?: limitOrError

            checkData += row
        }

        return checkData
    }

    private const val SELECT_LIMIT_ACCOUNT = "select od.PTKB_PLASTIC_TURNOUT.getLimitCardAccount(?) from dual"

    private fun titleHtml() = "Сверка остатков на ${LocalDate.now()}"

    private val HEADER_TABLE = mapOf(
            "Счет" to "left",
            "Клиент" to "left",
            "Остаток в ПЦ"  to "right",
            "Остаток на основ. счете"  to "right",
            "Остаток на внебаланс. счете"  to "right",
            "Остаток на техн. овер"  to "right",
            "Заблокированные суммы" to "right",
            "Лимит по счету" to "right",
            "Сумма несовпадения"  to "right",
            "Последняя операция" to "center")

    private const val SELECT_REVIEW = "{ ? = call od.PTKB_PLASTIC_TURNOUT.getReviewRestAllCard(sysdate) }"

    private fun isContainsName(name: String): Boolean = name.indexOf(ctlToday() ) >= 0

    private fun ctlToday() = "CTL${dateFormat(LocalDateTime.now())}_0226.0001"

    private fun dateFormat(date: LocalDateTime) = DateTimeFormatter.ofPattern("yyyyMMdd").format(date)
}