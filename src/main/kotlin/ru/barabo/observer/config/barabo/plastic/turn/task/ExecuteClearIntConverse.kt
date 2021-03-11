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

object ExecuteClearIntConverse : SingleSelector {

    override val select: String =
        "select c.id, c.file_name from od.ptkb_clearint c where c.state = 1 and trunc(ondate) = trunc(sysdate)"

    override val accessibleData: AccessibleData = AccessibleData(
        workTimeFrom = LocalTime.of(9, 0),
        workTimeTo =  LocalTime.of(23, 45),
        executeWait = Duration.ofSeconds(1) )

    override fun name(): String = "CLEARINT конверт. в руб."

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        val isExists = (AfinaQuery.selectValue(IS_EXISTS_CONVERSE, arrayOf(elem.idElem) ) as? Number)?.toInt() ?: 0

        if(isExists == 0) {
            return sendMailAbsentConverse(elem.name.substringBefore('.'))
        }

        AfinaQuery.execute(EXEC_CLEARINT_CONVERSE, arrayOf(elem.idElem))

        sendMailConverseInfo(elem.idElem!!, elem.name.substringBefore('.'))

        return State.OK
    }

    private fun sendMailAbsentConverse(fileName: String): State {

        val info = "В файле $fileName не было ни одной записи возмещения в валюте"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.UOD, bcc = BaraboSmtp.OPER_YA,
            subject = info, body = info, charsetSubject = "UTF-8")

        return State.OK
    }

    private fun sendMailConverseInfo(idClearInt: Number, fileName: String) {
        val info = AfinaQuery.execute(query = EXEC_GET_INFO_CLEARINT, params = arrayOf(idClearInt),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as? String ?:""

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.UOD, cc = BaraboSmtp.OPER_YA,
            subject = SUBJECT_EXEC + fileName, body = info, charsetSubject = "UTF-8")
    }
}

private const val SUBJECT_EXEC = "Возмещение в руб. эквиваленте "

private const val EXEC_CLEARINT_CONVERSE = "{ call od.PTKB_PLASTIC_TURN.processClearIntCreateConverse(?) }"

private const val IS_EXISTS_CONVERSE = "select od.PTKB_PLASTIC_TURN.isExistsClearIntConvert(?) from dual"

private const val EXEC_GET_INFO_CLEARINT = "{ call od.PTKB_PLASTIC_TURN.getInfoProcessedConverse(?, ?) }"