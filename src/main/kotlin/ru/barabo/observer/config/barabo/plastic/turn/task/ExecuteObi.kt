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

object ExecuteObi: SingleSelector {

    override val select: String = "select o.id, o.FILE_NAME from od.ptkb_obi o where o.state = 0 and " +
            "exists(select 1 from od.ptkb_transact_obi where OBI = o.id) order by o.PC_CREATE"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
            workTimeTo =  LocalTime.of(23, 45), executeWait = Duration.ofMinutes(1))

    override fun name(): String = "OBI_GC_FEE Обработать"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_OBI, arrayOf(elem.idElem))

        val info = AfinaQuery.execute(query = CALL_INFO_OBI, params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String

        val (cc, bcc, subject) = if(ExecuteCtlMtl.isNoneExecAllDocuments(info))
            Triple(BaraboSmtp.AUTO, emptyArray(), SUBJECT_NONE_EXEC) else
            Triple(BaraboSmtp.CHECKER_PLASTIC, BaraboSmtp.YA, SUBJECT_ALL_EXEC)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.DELB_PLASTIC, cc = cc, bcc = bcc, subject = subject, body = info?:"",
                charsetSubject = "UTF-8")

        return State.OK
    }

    private const val SUBJECT_ALL_EXEC ="Пластик: Информация по обработанному файлу OBI_GC_FEE"

    private const val SUBJECT_NONE_EXEC ="\u265E\u2602\u2622\u260E\u265E\u2602\u2622\u260E Пластик: Не все док-ты обработаны в файле OBI_GC_FEE"

    private const val EXEC_OBI = "{ call od.PTKB_PLASTIC_TURN.processsObiBySchema(?) }"

    private const val CALL_INFO_OBI = "{ call od.PTKB_PLASTIC_TURN.getInfoProcessedObi(?, ?) }"
}