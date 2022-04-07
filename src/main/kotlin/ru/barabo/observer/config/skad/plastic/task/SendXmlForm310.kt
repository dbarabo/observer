package ru.barabo.observer.config.skad.plastic.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.forms.form310.impl.DefaultForm310Data
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

private val logger = LoggerFactory.getLogger(SendXmlForm310::class.java)

object SendXmlForm310 : SingleSelector  {
    override fun name(): String = "Отправка 310-й формы на email"

    override fun config(): ConfigTask = PlasticOutSide

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
        LocalTime.of(8, 0), LocalTime.of(21, 0), Duration.ofSeconds(1))

    override val select: String = """select r.id, r.creator || ' ' || to_char(r.reportdate, 'dd.mm.yy') 
|| ' ' || to_char(r.created, 'dd.mm.yy hh24:mi:ss'), TYPE_FORM from od.PTKB_FORM_310_REQUEST r where r.state = 0"""

    override fun actionTask(selectorValue: Any?): ActionTask {
        return when {
            selectorValue == null -> this
            (selectorValue as Number).toInt() == 0 -> this
            selectorValue.toInt() == 1 -> SendXmlRiskClientCbr
            else -> this
        }
    }

    override fun execute(elem: Elem): State {

        val infoMail = AfinaQuery.select(SELECT_MAIL, arrayOf(elem.idElem)).takeIf { it.isNotEmpty() }?.get(0) ?: return State.ARCHIVE

        val mail = infoMail[0] as? String

        val error = infoMail[1] as? String

        val reportDate = (infoMail[2] as Date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if(mail.isNullOrEmpty() || (!error.isNullOrEmpty())) {
            return sendError(elem, mail, if(error.isNullOrEmpty())"mail is NULL" else error)
        }

        try {
            val form310 = DefaultForm310Data(reportDate)

            val fileForm = form310.createFile()

            sendFileMail(mail, reportDate, fileForm)

            updateStateOk(elem)

        } catch (e: Exception) {
            logger.error("SendXmlForm310", e)

            sendError(elem, mail, e.message?:"")

            elem.error = e.message

            return State.ERROR
        }

        return State.OK
    }

    private fun sendFileMail(mail: String?, reportDate: LocalDate, file: File) {
        BaraboSmtp.sendStubThrows(mail?.let { arrayOf(it) } ?: emptyArray(),
            cc = BaraboSmtp.OPER,
            subject = "310-я Форма на $reportDate",
            body = "310-я Форма на $reportDate",
            attachments = arrayOf(file)
        )
     }


    private fun sendError(elem: Elem, mail: String?, error: String): State {

        AfinaQuery.execute(UPDATE_ERROR_310, arrayOf(error, elem.idElem))

        BaraboSmtp.sendStubThrows(mail?.let { arrayOf(it) } ?: emptyArray(),
            cc = BaraboSmtp.AUTO,
            subject = "Ошибка при выгрузке xml 310-й формы",
            body = error
        )

        elem.error = error

        return State.ERROR
    }

}

internal fun updateStateOk(elem: Elem) {
    AfinaQuery.execute(UPDATE_OK, arrayOf(elem.idElem))
}

private const val SELECT_MAIL = "select r.MAIL, r.ERRORTEXT, r.reportdate from od.PTKB_FORM_310_REQUEST r where r.id = ?"

const val UPDATE_ERROR_310 = "update od.PTKB_FORM_310_REQUEST set state = 2, UPDATED = sysdate, ERRORTEXT = ? where id = ?"

private const val UPDATE_OK = "update od.PTKB_FORM_310_REQUEST set state = 1, UPDATED = sysdate where id = ?"