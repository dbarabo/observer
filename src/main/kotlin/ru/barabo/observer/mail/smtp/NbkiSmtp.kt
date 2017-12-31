package ru.barabo.observer.mail.smtp

import ru.barabo.smtp.SendMail
import ru.barabo.smtp.SmtpProperties

object NbkiSmtp : SendMail {
    override val smtpProperties: SmtpProperties = SmtpProperties(
            host = "ns.ptkb.ru",
            user = "nbki",
            password = "xbckjdjq",
            from = "nbki@ptkb.ru")
}