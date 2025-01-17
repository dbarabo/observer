package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.SQLException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

object Process440p : SingleSelector {

    override fun name(): String = "Обработка данных"

    override fun config(): ConfigTask = EnsConfig //P440Config

    override val select: String = "select id, FILE_NAME from od.ptkb_440p_fns_from where state = 0"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
            workTimeTo = LocalTime.of(18, 0), executeWait = Duration.ofSeconds(5))

    const val EXEC_440P = "{ call od.PTKB_440P.processRaise(?) }"

    override fun execute(elem: Elem): State {

        try {
            AfinaQuery.execute(EXEC_440P, arrayOf(elem.idElem) )

            if(elem.name.indexOf("RPO") == 0) {

                RooWaitCancel.execute(elem)
            }

        } catch (e: Exception) {
            if(isVipException(e.message)) {
                return sendVipInfo(elem, e.message!!)
            } else {
                throw SQLException(e.message)
            }
        }

        return State.OK
    }

    private fun isVipException(exception :String?): Boolean = (((exception?.indexOf("!VIP!") ?: -1) >= 0))

    private const val EXEC_STATE_TO_VIP = "{ call od.PTKB_440P.changeStateToVip(?) }"

    private const val SUBJECT_VIP = "440-П VIP"

    private val WAIT_VIP = Duration.ofHours(4)

    private fun sendVipInfo(elem: Elem, vipInfo :String): State {

        AfinaQuery.execute(EXEC_STATE_TO_VIP, arrayOf(elem.idElem) )

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, bcc = BaraboSmtp.YA, subject = SUBJECT_VIP, body = vipInfo)

        elem.executed = LocalDateTime.now().plusSeconds(WAIT_VIP.seconds)

        return State.NONE
    }
}