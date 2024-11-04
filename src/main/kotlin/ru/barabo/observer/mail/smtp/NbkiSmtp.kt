package ru.barabo.observer.mail.smtp

import ru.barabo.observer.crypto.MasterKey
import ru.barabo.smtp.SendMail
import ru.barabo.smtp.SmtpProperties
import java.io.File

object NbkiSmtp : SendMail {
    override val smtpProperties: SmtpProperties = SmtpProperties(
            host = "ns.ptkb.ru",
            user = "nbki",
            password = MasterKey.value("NBKI_PSWD"),
            from = "nbki@ptkb.ru")

    private val NBKI =  arrayOf("GUTDF@nbki.ru") //arrayOf("RUTDF@nbki.ru") //arrayOf("credithistory@nbki.ru")  //arrayOf("ChangeCreditHistory@nbki.ru")

    private val SUBJECT_NBKI = "ООО \"Примтеркомбанк\""//"NBKI information"

    private val BODY_NBKI = "information about loans from PTKB bank" //"file contains xlsx file"

    fun sendToNbki(file: File) {

        NbkiSmtp.send(to = NBKI, cc = BaraboSmtp.AUTO, subject = SUBJECT_NBKI, body = BODY_NBKI, attachments = arrayOf(file))
    }
}