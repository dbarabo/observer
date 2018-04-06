package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo.hCardOutSentTodayFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object OutRestCheck: SingleSelector {

    override val select: String = "select id, file_name from od.ptkb_plastic_acc where trunc(created) = trunc(sysdate)"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "Сверка остатков с ПЦ"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        if(!isCtlExecAllDocumentFound()) {
            return if(isEndTime() ) processNoneExecAllDocument(elem) else waitToNextTime(elem)
        }

        return processExecAllDocument(elem)
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

    private fun processNoneExecAllDocument(elem: Elem): State {

        //val file = createReview(elem.name)

        //file?.let { sendMailFile(NONE_EXEC_SUBJECT, it) }

        val data = createHtmlData() ?: return State.OK

        sendMailFile(NONE_EXEC_SUBJECT, data)

        return State.OK
    }

    private fun processExecAllDocument(elem: Elem): State {

        //val file = createReview(elem.name)

        val data = createHtmlData() ?: return State.OK

        sendMailFile(EXEC_SUBJECT, data)

        //file?.let { sendMailFile(EXEC_SUBJECT, it) }

        return State.OK
    }

    private fun createReview(nameRest: String): File? {

        val data = createHtmlData() ?: return null

        val file = fileReview(nameRest)

        file.writeText(data, Charset.forName("CP1251"))

        return file
    }

    private fun fileReview(nameRest: String) = File("${hCardOutSentTodayFolder()}/$nameRest.html")

    private fun sendMailFile(subject: String, data: String) {
        BaraboSmtp.sendStubThrows( to = BaraboSmtp.DELB_PLASTIC, bcc = BaraboSmtp.AUTO, subject = subject,
                body = data, subtypeBody = "html") //, attachments = arrayOf(fileSend))
    }

    private const val NONE_EXEC_SUBJECT = "✖✖✖ Сверка остатков из Картстандарта - есть неисполненные док-ты"

    private const val EXEC_SUBJECT = "Сверка остатков Картстандарта и Афины"

    private fun bodyRest(file: File) = "В файле вложения ${file.name} находится таблица сверки остатков\n" +
            "Отсутствие данных в таблице означает, что расхождений остатков нет"

    private fun createHtmlData(): String? {

        val data = AfinaQuery.selectCursor(SELECT_REVIEW)

        if(data.isEmpty()) return null

        val content = HtmlContent(titleHtml(), titleHtml(), HEADER_TABLE, data)

        return content.html()
    }

    private fun titleHtml() = "Сверка остатков на ${LocalDate.now()}"

    private val HEADER_TABLE = mapOf(
            "Счет" to "left",
            "Клиент" to "left",
            "Остаток в ПЦ"  to "right",
            "Остаток на основ. счете"  to "right",
            "Остаток на внебаланс. счете"  to "right",
            "Остаток на техн. овер"  to "right",
            "Сумма несовпадения"  to "right",
            "Последняя операция" to "center")

    private const val SELECT_REVIEW = "{ ? = call od.PTKB_PLASTIC_TURNOUT.getReviewRestAllCard(sysdate) }"

    private fun isContainsName(name: String): Boolean = name.indexOf(ctlToday() ) >= 0

    private fun ctlToday() = "CTL${dateFormat(LocalDateTime.now())}_0226.0001"

    private fun dateFormat(date: LocalDateTime) = DateTimeFormatter.ofPattern("yyyyMMdd").format(date)
}