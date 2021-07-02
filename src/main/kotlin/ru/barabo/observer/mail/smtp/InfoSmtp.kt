package ru.barabo.observer.mail.smtp

import ru.barabo.observer.config.cbr.sender.task.EmailTempSender
import ru.barabo.observer.crypto.MasterKey
import ru.barabo.smtp.SendMail
import ru.barabo.smtp.SmtpProperties

object InfoSmtp : SendMail {
    override val smtpProperties: SmtpProperties = SmtpProperties(
        host = "mail",
        user = "info",
        password = MasterKey.value("SMTP_INFO_PSWD"),
        from = MasterKey.value("SMTP_INFO") )

    fun sendToTest() {

        send(to = BaraboSmtp.AUTO, subject = "test", body = "test")
    }
}