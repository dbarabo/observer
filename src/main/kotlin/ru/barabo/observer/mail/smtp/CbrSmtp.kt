package ru.barabo.observer.mail.smtp

import ru.barabo.smtp.SendMail
import ru.barabo.smtp.SmtpException
import ru.barabo.smtp.SmtpProperties
import java.io.File

object CbrSmtp : SendMail {
    override val smtpProperties: SmtpProperties = SmtpProperties(
    host ="12.70.91.5",
    user = "0507717",
    password = "Pt07kb717",
    from = "0507717@svk.vladivostok.cbr.ru")


    private val PRIKAZ_TO = arrayOf("prikazbr@vladivostok.cbr.ru")

    val SUBJECT_CBR_SEND_ERROR = "CBR SMTP SEND ERROR"

    private fun bodyError(error :String, file :File) = "Ошибка отправки файла в ЦБ файл=${file.absolutePath}\nошибка=$error"

    fun sendCbInfo(file : File) {

        try {
            send(to = PRIKAZ_TO, subject = "INFO", body = "info", attachments = arrayOf(file) )
        } catch (e : SmtpException) {

            BaraboSmtp.errorSend(bodyError(e.message, file), SUBJECT_CBR_SEND_ERROR)
        }
    }
}