package ru.barabo.smtp

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.store.Shift
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMultipart
import javax.mail.search.AndTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.FlagTerm
import javax.mail.search.ReceivedDateTerm

interface GetMail : SendMail {
    val mailProperties: MailProperties

    val findFromEncrypt: String

    val subjectStartSelect: String

    val subjectStartExec: String

    override val smtpProperties
            get() = SmtpProperties(host = mailProperties.hostSmtp, user = mailProperties.user,
                    password = mailProperties.password, from = mailProperties.user)


    override fun propSmtp() :Properties = super.propSmtp().apply {
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.port", "587")
    }

    private fun checkStartSelect() = "SELECT "

    private fun checkStartExec() = "{ call"

    private fun inbox() = "INBOX"

    fun isErrorToLog() = true

    fun checkMessages() {

        try {
            val store = connectGetMail()

            store.getFolder(inbox()).checkMessages()

            store.close()
        } catch (e: Exception) {
            if(isErrorToLog() ) LoggerFactory.getLogger(GetMail::class.java).error("checkMessages", e)
        }
    }

    private fun connectGetMail(): Store {

        val store = mailSession().getStore(mailProperties.protocol)

        store.connect(mailProperties.user, mailProperties.password)

        return store
    }

    @Synchronized
    private fun mailSession(): Session =  Session.getInstance(propMail(), authenticator() ).apply { debug = false }


    private fun propMail(): Properties = Properties().apply {
        put("mail.store.protocol", mailProperties.protocol)

        put("mail.pop3.host", mailProperties.host)
        put("mail.imaps.host", mailProperties.host)

        put("mail.pop3.port", mailProperties.port.toString())
        put("mail.imaps.port", mailProperties.port.toString())
    }

    private fun authenticator() : Authenticator? {

        return object : Authenticator() {
            override fun getPasswordAuthentication() : PasswordAuthentication =
                    PasswordAuthentication(mailProperties.user, mailProperties.password)
        }
    }

    private fun Folder.checkMessages() {
        open(Folder.READ_WRITE)

        val seenFlag = Flags(Flags.Flag.SEEN)

        val unSeenFlagTerm = FlagTerm(seenFlag, false)

        val today = ReceivedDateTerm(ComparisonTerm.EQ, Date())

        val restriction = AndTerm(unSeenFlagTerm, today)

        val messages = search(restriction)

        try {
            messages.forEach { it.process() }
        } catch (e: MessagingException) {

            if(isErrorToLog()) LoggerFactory.getLogger(GetMail::class.java).error("checkMessages.messages", e)

            close(false)

            throw MessagingException(e.message)
        }

        close(false)
    }

    private fun Message.process() {

        from.firstOrNull {  (it as? InternetAddress)?.address?.toUpperCase()?.indexOf(
                Shift.decrypt(findFromEncrypt).toUpperCase())?:-1 >= 0} ?: return

        val processor = processorBySubject(subject)?: return

        val body = (content as? MimeMultipart)?.textBody() ?: return

        processor(body, subject)

        setFlag(Flags.Flag.SEEN, true)
    }

    private fun MimeMultipart.textBody(): String? = getBodyPart(0)?.content as? String

    private fun processorBySubject(subject: String?): ((String, String)->Unit)? {

        val subj =  subject?.toUpperCase()?: return null

        return when {
            subj.indexOf(subjectStartSelect) == 0 -> ::processSelect

            subj.indexOf(subjectStartExec) == 0 -> ::processExec

            else -> null
        }
    }

    private fun processSelect(select: String, subject: String) {

        //LoggerFactory.getLogger(GetMail::class.java).error("processSelect subject=$subject")

        //LoggerFactory.getLogger(GetMail::class.java).error("processSelect select=$select")

        if(select.trim().indexOf(checkStartSelect() ) != 0) return

        val text = try {
            AfinaQuery.select(select).joinToString("\n"){ it.joinToString("\t") }
        } catch (e: Exception) {
            e.message!!
        }

        //LoggerFactory.getLogger(GetMail::class.java).error("text=$text")

        sendResponseSelect(text, subject)
    }

    private fun sendResponseSelect(text: String, subject: String) {

        val subj = responseSubject(subject, subjectStartSelect, ::responseSelect)

        sendStubThrows(to = arrayOf(Shift.decrypt(findFromEncrypt)), subject = subj, body = text)
    }

    private fun responseSelect() = "RESPONSE SELECT"

    private fun processExec(exec: String, subject: String) {
        if(exec.indexOf(checkStartExec() ) != 0) return

        val text = try {
            AfinaQuery.execute(exec)
            "Ok"
        } catch (e: Exception) {
            e.message!!
        }

        sendResponseExecute(text, subject)
    }

    private fun sendResponseExecute(text: String, subject: String) {

        val subj = responseSubject(subject, subjectStartExec, ::responseExec)

        sendStubThrows(to = arrayOf(Shift.decrypt(findFromEncrypt)), subject = subj, body = text)
    }

    private fun responseExec() = "RESPONSE EXEC"

    private fun responseSubject(subject: String, subjFirstStart: String, responseSubj: ()->String): String =
            "${responseSubj()} ${subject.substring(subjFirstStart.length - 1)}"
}