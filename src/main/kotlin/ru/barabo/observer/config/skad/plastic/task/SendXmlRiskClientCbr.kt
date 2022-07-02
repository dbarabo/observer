package ru.barabo.observer.config.skad.plastic.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.forms.clientrisk.impl.DefaultClientRisk
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

private val logger = LoggerFactory.getLogger(SendXmlRiskClientCbr::class.java)

object SendXmlRiskClientCbr : SingleSelector {

    override fun name(): String = "Отправка XML - Клиентов с Рисками ЦБР"

    override fun config(): ConfigTask = PlasticOutSide

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.ALL_DAYS, false,
        LocalTime.of(8, 0), LocalTime.of(21, 0), Duration.ofSeconds(1))

    override val select: String = "select * from dual where 1 = 0"

    override fun execute(elem: Elem): State {

        val infoMail = AfinaQuery.select(SELECT_MAIL, arrayOf(elem.idElem)).takeIf { it.isNotEmpty() }?.get(0) ?: return State.ARCHIVE

        val mail = infoMail[0] as? String

        val error = infoMail[1] as? String

        if(mail.isNullOrEmpty() || (!error.isNullOrEmpty())) {
            return sendError(elem, mail, if (error.isNullOrEmpty()) "mail is NULL" else error)
        }

        //val codeRisk = (infoMail[2] as Number).toInt()

        val typeClient = (infoMail[3] as? String)?.toInt()

        try {
            val formRisk = DefaultClientRisk(typeClient)

            val fileForm = formRisk.createFile()

            sendFileMail(mail, fileForm)

            updateStateOk(elem)

        } catch (e: Exception) {
            logger.error("SendXmlForm310", e)

            sendError(elem, mail, e.message ?: "")

            elem.error = e.message

            return State.ERROR
        }

        return State.OK
    }

    private fun sendError(elem: Elem, mail: String?, error: String): State {

        AfinaQuery.execute(UPDATE_ERROR_310, arrayOf(error, elem.idElem))

        BaraboSmtp.sendStubThrows(mail?.let { arrayOf(it) } ?: emptyArray(),
            cc = BaraboSmtp.AUTO,
            subject = "Ошибка при выгрузке xml Риски по клиентам для ЦБ",
            body = error
        )

        elem.error = error

        return State.ERROR
    }

    private fun sendFileMail(mail: String?, file: File) {
        BaraboSmtp.sendStubThrows(mail?.let { arrayOf(it) } ?: emptyArray(),
            cc = BaraboSmtp.OPER,
            subject = "Риски по клиентам для ЦБ",
            body = "Риски по клиентам для ЦБ",
            attachments = arrayOf(file)
        )
    }
}

private const val SELECT_MAIL = "select r.MAIL, r.ERRORTEXT, r.INFO_INTEGER, r.INFO_STRING from od.PTKB_FORM_310_REQUEST r where r.id = ?"