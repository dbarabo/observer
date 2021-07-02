package ru.barabo.observer.config.cbr.sender.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.sender.SenderMail
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.InfoSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

object EmailTempSender : SingleSelector {

    override fun name(): String = "Отправка из PTCB_EMAILTEMP"

    override fun config(): ConfigTask = SenderMail

    override val select: String = "select CLASSIFIED, SUBJECT || '>> ' || EMAIL from od.PTCB_EMAILTEMP"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(1, 0),
        workTimeTo = LocalTime.of(23, 55))

    override fun execute(elem: Elem): State {

        val info = AfinaQuery.select(SELECT_INFO, arrayOf(elem.idElem))

        if(info.isEmpty()) return State.ARCHIVE

        val (emailAll, subject, description) = info[0]

        val emails = (emailAll as? String)?.split(";")?.toTypedArray()
            ?: return State.ARCHIVE.deleteEmailReturn(elem.idElem)

        InfoSmtp.send(to = emails, subject = subject as? String ?: "", body = description as? String ?: "")

        return State.OK.deleteEmailReturn(elem.idElem)
    }
}

private fun State.deleteEmailReturn(idElem: Long?): State {

    AfinaQuery.execute(DELETE_EMAIL, arrayOf(idElem) )

    return this
}

private const val SELECT_INFO = "select EMAIL, SUBJECT, DESCRIPTION from od.PTCB_EMAILTEMP where CLASSIFIED = ?"

private const val DELETE_EMAIL = "delete from od.PTCB_EMAILTEMP where CLASSIFIED = ?"