package ru.barabo.observer.mail.smtp

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.crypto.MasterKey
import ru.barabo.smtp.SendMail
import ru.barabo.smtp.SmtpException
import ru.barabo.smtp.SmtpProperties
import java.io.File

object CbrSmtp : SendMail {
    override val smtpProperties: SmtpProperties = SmtpProperties(
    host ="12.70.91.5",
    user = "0507717",
    password = MasterKey.value("CBR_PSWD"),
    from = "0507717@svk.vladivostok.cbr.ru")


    private val PRIKAZ_TO = arrayOf("prikazbr@vladivostok.cbr.ru")

    private fun bodyError(error: String, file: File) = "Ошибка отправки файла в ЦБ файл=${file.absolutePath}\nошибка=$error"

    fun sendCbInfo(file : File) {

        try {
            send(to = PRIKAZ_TO, subject = "INFO", body = "info", attachments = arrayOf(file) )
        } catch (e : SmtpException) {

            BaraboSmtp.errorSend(bodyError(e.message, file), SUBJECT_CBR_SEND_ERROR)
        }
    }

    fun checkCbr() {
        try {
            send(to = arrayOf(smtpProperties.from), subject = "INFO", body = "info" )
        } catch (e: SmtpException) {

            BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, bcc = BaraboSmtp.AUTO, subject = SUBJECT_CBR_SEND_ERROR,
                body = BODY_CBR_SEND_ERROR)

            try {
                sentErrorMessage()
            } catch (e: SmtpException) {
                LoggerFactory.getLogger(SendMail::class.java).error("checkCbr", e)
            }
        }
    }
}

private const val SUBJECT_CBR_SEND_ERROR = "Нет связи с ЦБ"

private const val BODY_CBR_SEND_ERROR = "Нет связи с ЦБ. Восстановите связь с ЦБ на 29-й компе"

private fun sentErrorMessage() {

    val to = BaraboSmtp.TTS[0]

    val bcc = BaraboSmtp.AUTO[0]

    val subject = SUBJECT_CBR_SEND_ERROR

    val body = BODY_CBR_SEND_ERROR

    AfinaQuery.execute(SEND_ERROR, arrayOf(to, subject, body, bcc))
}

private const val SEND_ERROR = "{ call od.PTKB_SENDMAIL.sendSimple( ?, ?, ?, ? ) }"