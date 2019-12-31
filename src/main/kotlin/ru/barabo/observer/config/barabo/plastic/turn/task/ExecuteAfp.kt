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

    override val select: String = """
        select a.id, a.FILE_NAME || ' (' || a.CHECK_COUNT_AUTH || ')' name 
        from od.ptkb_afp a where a.state = 0 and nvl(a.CHECK_COUNT_AUTH, 0) != 0 
        and exists (select 1 from od.ptkb_afp_record fr where fr.afp = a.id and fr.state = 0)
        order by a.PC_CREATED, a.FILE_ORDER
    """

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(7, 45),
            workTimeTo =  LocalTime.of(23, 0), executeWait = Duration.ofMinutes(1))

    override fun name(): String = "AFP Обработать"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_AFP, arrayOf(elem.idElem))

        val info = AfinaQuery.execute(query = CALL_INFO_CTL, params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String

        if(isNoneExecAllDocuments(info)) {
            BaraboSmtp.sendStubThrows(to = BaraboSmtp.DELB_PLASTIC, cc = BaraboSmtp.AUTO,
                    subject = SUBJECT_NONE_EXEC, body = info?:"", charsetSubject = "UTF-8")
        }

        return State.OK
    }

    private const val SUBJECT_NONE_EXEC = "✖✖✖☹☹☹✚✚✚☝☝☝✠✠✠♕♕♕ Пластик: Не все док-ты обработаны в файле AFP"

    private fun isNoneExecAllDocuments(info :String?) :Boolean = info?.indexOf(CHECK_ALL_EXEC_DOCUMENTS)?:-1 < 0

    private const val CHECK_ALL_EXEC_DOCUMENTS =  ". НЕ Исполнено платежных документов <0>"

    private const val EXEC_AFP = "{ call od.PTKB_PLASTIC_TURN.processAfpBySchema(?) }"

    private const val CALL_INFO_CTL = "{ call od.PTKB_PLASTIC_TURN.getInfoProcessedAfp(?, ?) }"
}