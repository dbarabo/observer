package ru.barabo.observer.config.skad.acquiring.task

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.acquiring.Acquiring
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object MinComissionMonthPos : Periodical {

    override fun name(): String = "Недобранные комиссии по терминалам"

    override fun config(): ConfigTask = Acquiring

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(3, 30), workTimeTo = LocalTime.of(10, 30),
        executeWait = Duration.ofMinutes(1) )

    override fun isAccess()  = super.isAccess() && (LocalDate.now().dayOfMonth == 1)

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_CREATE_COMISSION)

        AfinaQuery.execute(EXEC_EXECUTE_COMISSION)

        sendMailReport()

        return State.OK
    }

    private fun sendMailReport() {
        val title = title()

        val html = createHtmlData(title) ?: return

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.UOD, cc = BaraboSmtp.TTS, bcc = BaraboSmtp.AUTO,
            subject = title, body = html, subtypeBody = "html")
    }

    private fun createHtmlData(title: String): String? {

        val data = AfinaQuery.selectCursor(SELECT_MIN_COMMISSION_DOC)

        if(data.isEmpty()) return null

        val content = HtmlContent(title, title, headerTable, data)

        return content.html()
    }

    private fun title() = "Список документов комиссий за эквайринговые операции по МИН. сумме 1999руб за ${
        java.text.SimpleDateFormat(
            "MMM"
        ).format(java.util.Date())}"

    private val headerTable = mapOf (
        "Терминал" to "left",
        "№ счета" to "left",
        "Клиент" to "left",
        "№ испол. док-та" to "left",
        "Дата испол. док-та" to "center",
        "Сумма испол. док-та" to "right",
        "№ док-та на картотеке" to "left",
        "Дата док-та на картотеке" to "center",
        "Сумма док-та на картотеке" to "right"
    )

    private const val EXEC_CREATE_COMISSION = "{ call od.PTKB_PLASTIC_TURN.processMinComissionMonthPos }"

    private const val EXEC_EXECUTE_COMISSION = "{ call od.PTKB_PLASTIC_TURN.executeMinComissionMonthPos }"

    private const val SELECT_MIN_COMMISSION_DOC = "{ ? = call od.PTKB_PLASTIC_TURN.getMinComissionMonthDocuments }"
}