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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object OutRestCheck: SingleSelector {

    override val select: String = "select id, file_name from od.ptkb_plastic_acc where trunc(created) = trunc(sysdate)"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "Сверка остатков с ПЦ"

    override fun config(): ConfigTask = PlasticTurnConfig

    private const val NEXT_TIME_MINUTE = 10L

    override fun execute(elem: Elem): State {

        if(!isCtlExecFound() ) {

            elem.executed = LocalDateTime.now().plusMinutes(NEXT_TIME_MINUTE)

            return State.NONE
        }

        val data = createHtmlData()

        val file = File("${hCardOutSentTodayFolder()}/${elem.name}.html")

        file.writeText(data, Charset.forName("CP1251"))

        BaraboSmtp.sendStubThrows( to = BaraboSmtp.DELB_PLASTIC, bcc = BaraboSmtp.AUTO, subject = SUBJECT_REST,
                body = bodyRest(file), attachments = arrayOf(file))

        return State.OK
    }

    private fun bodyRest(file: File) = "В файле вложения ${file.name} находится таблица сверки остатков\n" +
            "Отсутствие данных в таблице означает, что расхождений остатков нет"

    private const val SUBJECT_REST = "Сверка остатков Картстандарта и Афины"

    private fun createHtmlData(): String {

        val data = AfinaQuery.selectCursor(SELECT_REST)

        val content = HtmlContent("Сверка остатков", "Сверка остатков", HEADER_TABLE, data)

        return content.html()
    }

    private val HEADER_TABLE = mapOf<String, String>(
            "п/п" to "right",
            "Счет" to "left",
            "Клиент" to "left",
            "Дата остатка" to "center",
            "Остаток в ПЦ"  to "right",
            "Остаток в Афине"  to "right",
            "Off-line оборот"  to "right",
            "On-line оборот"  to "right",
            "Сумма несовпадения"  to "right")

    private const val SELECT_REST = "{ ? = call od.PTKB_PLASTIC_TURNOUT.getRestAfinaPc( sysdate, 0, 30) }"

    private fun isCtlExecFound(): Boolean =
            StoreSimple.findFirstByConditionName(task = ExecuteCtlMtl, isConditionName = ::isContainsName) != null

    private fun isContainsName(name: String): Boolean = name.indexOf(ctlToday() ) >= 0

    private fun ctlToday() = "CTL${dateFormat(LocalDateTime.now())}_0226.0001"

    private fun dateFormat(date: LocalDateTime) = DateTimeFormatter.ofPattern("yyyyMMdd").format(date)
}