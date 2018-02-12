package ru.barabo.observer.config.barabo.plastic.turn.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object ExecuteAfp: SingleSelector {

    override val select: String = "select id, FILE_NAME || ' (' || CHECK_COUNT_AUTH || ')' name " +
            "from od.ptkb_afp where state = 0 and nvl(CHECK_COUNT_AUTH, 0) != 0 order by PC_CREATED, FILE_ORDER"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
            workTimeTo =  LocalTime.of(23, 45), executeWait = Duration.ofMinutes(1))

    override fun name(): String = "AFP Обработать"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_AFP, arrayOf(elem.idElem))

        val info = AfinaQuery.execute(query = CALL_INFO_CTL, params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String

        val (cc, bcc, subject) = if(isNoneExecAllDocuments(info))
            Triple(BaraboSmtp.AUTO, emptyArray(), SUBJECT_NONE_EXEC) else
            Triple(BaraboSmtp.CHECKER_PLASTIC, BaraboSmtp.YA, SUBJECT_ALL_EXEC)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.DELB_PLASTIC, cc = cc, bcc = bcc, subject = subject, body = info?:"", charsetSubject = "UTF-8")

        return State.OK
    }

    private const val SUBJECT_ALL_EXEC = "Пластик: Информация по обработанному файлу AFP"

    private const val SUBJECT_NONE_EXEC = "✖✖✖☹☹☹✚✚✚☝☝☝✠✠✠♕♕♕ Пластик: Не все док-ты обработаны в файле AFP"

    private fun isNoneExecAllDocuments(info :String?) :Boolean = info?.indexOf(CHECK_ALL_EXEC_DOCUMENTS)?:-1 < 0

    private const val CHECK_ALL_EXEC_DOCUMENTS =  ". НЕ Исполнено платежных документов <0>"

    private const val EXEC_AFP = "{ call od.PTKB_PLASTIC_TURN.processAfp(?) }"

    private const val CALL_INFO_CTL = "{ call od.PTKB_PLASTIC_TURN.getInfoProcessedAfp(?, ?) }"
}