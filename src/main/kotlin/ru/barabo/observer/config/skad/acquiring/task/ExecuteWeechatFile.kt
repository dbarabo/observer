package ru.barabo.observer.config.skad.acquiring.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.ExecuteCtlMtl
import ru.barabo.observer.config.skad.acquiring.Acquiring
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object ExecuteWeechatFile : SingleSelector {
    private val logger = LoggerFactory.getLogger(ExecuteWeechatFile::class.java)

    override fun name(): String = "Weechat Обработать"

    override fun config(): ConfigTask = Acquiring

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(7, 50),
            workTimeTo =  LocalTime.of(20, 0), executeWait = Duration.ofMinutes(1))

    override val select: String = "select w.id, w.file_name from od.ptkb_weechat w where w.state = 0 order by w.substr(file_name, 19)"

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(CREATE_WEECHAT, arrayOf(elem.idElem))

        try {
            AfinaQuery.execute(EXEC_WEECHAT, arrayOf(elem.idElem))


        } catch (e: Exception) {
            logger.error(EXEC_WEECHAT, e)

            elem.state = State.ERROR
            elem.error = e.message
            checkSendMailError(elem)
            elem.error = elem.error?.let{ if(it.length <= 500) it else it.substring(0, 500) }

            return State.OK
        }

        val info = AfinaQuery.execute(query = CALL_INFO_WEECHAT, params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String

        val (cc, bcc, subject) = if(ExecuteCtlMtl.isNoneExecAllDocuments(info))
            Triple(BaraboSmtp.AUTO, BaraboSmtp.DOPIKI, SUBJECT_NONE_EXEC) else
            Triple(BaraboSmtp.CHECKER_PLASTIC, BaraboSmtp.YA, SUBJECT_ALL_EXEC)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.IBANK_DELB, cc = cc, bcc = bcc, subject = subject, body = info?:"", charsetSubject = "UTF-8")

        return State.OK
    }

    private const val CREATE_WEECHAT = "{ call od.PTKB_SENDY.processCreateByFile(?) }"

    private const val EXEC_WEECHAT = "{ call od.PTKB_SENDY.executeWeechatDocuments(?) }"

    private const val CALL_INFO_WEECHAT = "{ call od.PTKB_SENDY.getInfoProcessedWeechat(?, ?) }"

    private const val SUBJECT_ALL_EXEC ="WEECHAT: Информация по обработанному файлу CTL/MTL"

    private const val SUBJECT_NONE_EXEC =
            "✖✖✖☹☹☹✚✚✚☝☝☝✠✠✠♕♕♕ WEECHAT: Не все док-ты обработаны в файле paymentsacq_daily"
}