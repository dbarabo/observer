package ru.barabo.observer.config.barabo.plastic.turn.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
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

object ExecuteCtlMtl : SingleSelector {

    private val logger = LoggerFactory.getLogger(ExecuteCtlMtl::class.java)

    override val select: String = "select id, FILE_NAME || ' (' || CHECK_COUNT_TRANSACT || ')' name " +
            "from od.ptkb_ctl_mtl where state = 0 and nvl(CHECK_COUNT_TRANSACT, 0) != 0 order by PC_CREATED, FILE_ORDER"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(7, 59),
            workTimeTo =  LocalTime.of(19, 0), executeWait = Duration.ofMinutes(1))

    override fun name(): String = "CTL/MTL Обработать"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        logger.error("before create ExecuteCtlMtl = ${LocalTime.now()}")
        AfinaQuery.execute(CREATE_CTL_MTL, arrayOf(elem.idElem))
        logger.error("after create ExecuteCtlMtl = ${LocalTime.now()}")

        try {
            AfinaQuery.execute(EXEC_CTL_MTL, arrayOf(elem.idElem))

            AfinaQuery.execute(CHECK_ACQ_FROM_MTL, arrayOf(elem.idElem) )

        } catch (e: Exception) {
            logger.error(EXEC_CTL_MTL, e)

            elem.state = State.ERROR
            elem.error = e.message
            checkSendMailError(elem)
            elem.error = elem.error?.let{ if(it.length <= 500) it else it.substring(0, 500) }

            return State.OK
        }
        logger.error("after execute ExecuteCtlMtl = ${LocalTime.now()}")

        val info = AfinaQuery.execute(query = CALL_INFO_CTL, params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String

        val (cc, bcc, subject) = if(isNoneExecAllDocuments(info))
            Triple(BaraboSmtp.AUTO, BaraboSmtp.DOPIKI, SUBJECT_NONE_EXEC) else
            Triple(BaraboSmtp.CHECKER_PLASTIC, BaraboSmtp.YA, SUBJECT_ALL_EXEC)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.DELB_PLASTIC, cc = cc, bcc = bcc, subject = subject, body = info?:"", charsetSubject = "UTF-8")

        return State.OK
    }

    private const val SUBJECT_ALL_EXEC ="Пластик: Информация по обработанному файлу CTL/MTL"

    private const val SUBJECT_NONE_EXEC =
            "✖✖✖☹☹☹✚✚✚☝☝☝✠✠✠♕♕♕ Пластик: Не все док-ты обработаны в файле CTL/MTL"

    fun isNoneExecAllDocuments(info :String?) :Boolean = info?.indexOf(CHECK_ALL_EXEC_DOCUMENTS)?:-1 < 0

    private const val CHECK_ALL_EXEC_DOCUMENTS =  ". НЕ Исполнено платежных документов <0>"

    private const val CREATE_CTL_MTL = "{ call od.PTKB_PLASTIC_TURN.procCtlMtlBySchema(?) }"

    private const val EXEC_CTL_MTL = "{ call od.PTKB_PLASTIC_TURN.executeCtlMtlDocuments(?) }"

    private const val CALL_INFO_CTL = "{ call od.PTKB_PLASTIC_TURN.getInfoProcessedCtl(?, ?) }"

    private const val CHECK_ACQ_FROM_MTL = "{ call od.PTKB_PLASTIC_TURN.checkAcqFromMtl(?) }"
}