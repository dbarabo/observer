package ru.barabo.observer.mail.smtp

import ru.barabo.observer.crypto.MasterKey
import ru.barabo.smtp.SendMail
import ru.barabo.smtp.SmtpProperties

object InfoSmtp : SendMail {
    override val smtpProperties: SmtpProperties = SmtpProperties(
        host = "mail", //"192.168.0.4"
        user = "info",
        password = MasterKey.value("SMTP_INFO_PSWD"),
        from = MasterKey.value("SMTP_INFO") )
}