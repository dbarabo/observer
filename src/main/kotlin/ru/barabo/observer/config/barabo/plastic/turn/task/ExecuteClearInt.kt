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

object ExecuteClearInt : SingleSelector {

    override val select: String =
        "select c.id, c.file_name from od.ptkb_clearint c where c.state = 0 and trunc(ondate) >= trunc(sysdate)-4"

    override val accessibleData: AccessibleData = AccessibleData(/*workWeek = WeekAccess.ALL_DAYS,*/
        workTimeFrom = LocalTime.of(7, 0),
        workTimeTo =  LocalTime.of(23, 45),
        executeWait = Duration.ofMinutes(1))

    override fun name(): String = "CLEARINT-создать проводки"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_CLEARINT, arrayOf(elem.idElem))

        sendMailInfoExec(elem.idElem as Number, elem.name.substringBefore('.') )

        //sendMailCorrespondInfo(elem.name.substringBefore('.'))

        return State.OK
    }

    private fun sendMailInfoExec(idClearInt: Number, fileName: String) {
        val info = AfinaQuery.execute(query = EXEC_GET_INFO_CLEARINT, params = arrayOf(idClearInt),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String ?:""

        val subject = if(info.contains("Внимание не все проводки исполнены!"))
            "✖✖✖☹☹☹✚✚✚☝☝☝✠✠✠♕♕♕ Внимание не все проводки исполнены! $fileName"
        else
            "Созданы проводки по платежным системам $fileName"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.CORRESPOND, bcc = BaraboSmtp.AUTO,
            subject = subject, body = info, charsetSubject = "UTF-8")
    }


    private fun sendMailCorrespondInfo(fileName: String) {

        val info = """Созданы проводки корсчета на основе файла $fileName
Завтра, после получения выписки от ПЦ, зайдите в архивную дату 21:20. 
Найдите общего папу с номером $fileName
Нажмите Shift+F9
Сверьте каждый внутренний перевод (для открытия перевода нажмите F7)
После сверки, если все сходится исполните общего папу (на папе правой кнопкой - исполнить)
"""

        val subject = "Созданы проводки по платежным системам $fileName"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.CORRESPOND, bcc = BaraboSmtp.AUTO,
            subject = subject, body = info, charsetSubject = "UTF-8")
    }
}

private const val EXEC_CLEARINT = "{ call od.PTKB_PLASTIC_TURN.processClearInt(?) }"

private const val EXEC_GET_INFO_CLEARINT = "{ call od.PTKB_PLASTIC_TURN.getInfoProcessedClearInt(?, ?) }"