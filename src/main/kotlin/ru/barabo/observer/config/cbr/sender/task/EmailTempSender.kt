package ru.barabo.observer.config.cbr.sender.task

import ru.barabo.html.HtmlContent
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

        val (emailAll, subject, description, ccSeparator, bccSeparator, cursor, selectParams, columns) = info[0]

        val emails = (emailAll as? String)?.split(";")?.toTypedArray()
            ?: return State.ARCHIVE.deleteEmailReturn(elem.idElem)

        val cc = (ccSeparator as? String)?.split(";")?.toTypedArray() ?: emptyArray()

        val bcc = (bccSeparator as? String)?.split(";")?.toTypedArray() ?: emptyArray()

        val subjectString = subject as? String ?: ""

        val body = description as? String ?: ""

        if((cursor as? String).isNullOrEmpty() ) {
            InfoSmtp.send(to = emails,
                cc = cc,
                bcc = bcc,
                subject = subjectString,
                body = body)

            return State.OK.deleteEmailReturn(elem.idElem)
        }

        return trySendHtmlTable(elem.idElem, emails, subjectString, body, cc, bcc, cursor as String,
            selectParams as? String, columns as String)
    }
}

private fun trySendHtmlTable(idElem: Long?, emails: Array<String>, subject: String, body: String,
             cc: Array<String>, bcc: Array<String>, cursor: String, selectParams: String?,
                             columns: String): State {

    val headerMap = columns.split(";")
        ?.toTypedArray()
        .associate { it to "left" }

    val params = selectParams?.takeIf { it.isNotBlank() }?.let { AfinaQuery.select(it)[0] }

    val data = AfinaQuery.selectCursor(cursorSelect(cursor), params)

    val content = HtmlContent(subject, body, headerMap, data).html()

    InfoSmtp.send(to = emails,
        cc = cc,
        bcc = bcc,
        subject = subject,
        body = content,
        subtypeBody = "html"
    )

    return State.OK.deleteEmailReturn(idElem)
}

private fun cursorSelect(cursor: String) = "{ ? = call $cursor }"

private operator fun <T> Array<T>.component8(): T = this[7]

private operator fun <T> Array<T>.component7(): T = this[6]

private operator fun <T> Array<T>.component6(): T = this[5]

private fun State.deleteEmailReturn(idElem: Long?): State {

    AfinaQuery.execute(DELETE_EMAIL, arrayOf(idElem) )

    return this
}

private const val SELECT_INFO =
    "select EMAIL, SUBJECT, DESCRIPTION, CC, BCC, SELECT_TABLE, SELECT_PARAMS, COLUMNS from od.PTCB_EMAILTEMP where CLASSIFIED = ?"

private const val DELETE_EMAIL = "delete from od.PTCB_EMAILTEMP where CLASSIFIED = ?"