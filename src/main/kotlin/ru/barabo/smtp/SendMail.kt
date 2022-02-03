package ru.barabo.smtp

import org.slf4j.LoggerFactory
import java.io.File
import java.io.UnsupportedEncodingException
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.*


interface SendMail {

    val smtpProperties: SmtpProperties

    fun propSmtp(): Properties {
        val properties = Properties()

        properties.setProperty("mail.transport.protocol", "smtp")
        properties.setProperty("mail.smtp.host", smtpProperties.host)
        properties.setProperty("mail.smtp.auth", smtpProperties.isAuth.toString())
        properties.setProperty("mail.smtp.user", smtpProperties.user)
        properties.setProperty("mail.smtp.password", smtpProperties.password)
        properties.setProperty("mail.smtp.from", smtpProperties.from)

        return properties
    }

    fun sendStubThrows(to :Array<String> = emptyArray(), cc :Array<String> = emptyArray(), bcc :Array<String> = emptyArray(),
                       subject :String, body :String, attachments :Array<File> = emptyArray(),
                       charsetSubject :String = smtpProperties.charsetSubject,
                       subtypeBody: String = "plain") {
        try {
            send(to, cc, bcc, subject, body, attachments, charsetSubject, subtypeBody)
        } catch (e :SmtpException) {
            LoggerFactory.getLogger(SendMail::class.java).error("sendStubThrows", e)

            LoggerFactory.getLogger(SendMail::class.java).error("body = $body")
        }
    }

    @Throws(SmtpException::class)
    fun send(to :Array<String> = emptyArray(), cc :Array<String> = emptyArray(), bcc :Array<String> = emptyArray(),
             subject :String, body :String, attachments :Array<File> = emptyArray(),
             charsetSubject :String = smtpProperties.charsetSubject,
             subtypeBody: String = "plain" ) {

        try {
            val smtpSession = smtpSession()

            synchronized(smtpSession) {
                val message = createMessage(smtpSession, subject, to, cc, bcc, charsetSubject)

                message.addPartsMessage(body, attachments, subtypeBody)

                smtpSession.messageSend(message)
            }

        } catch (e :Exception) {
            LoggerFactory.getLogger(SendMail::class.java).error("send", e)

            throw SmtpException(e.message ?: "")
        }
    }

    @Throws(NoSuchProviderException::class, MessagingException::class)
    private fun createMessage(session :Session, subject :String, to :Array<String>, cc :Array<String>,
                              bcc :Array<String>, charsetSubject :String = smtpProperties.charsetSubject) : MimeMessage {

        val message = MimeMessage(session)

        message.setSubject(subject, charsetSubject)

        message.setFrom(InternetAddress(smtpProperties.from))

        to.forEach { message.addRecipient(Message.RecipientType.TO, InternetAddress(it)) }

        cc.forEach { message.addRecipient(Message.RecipientType.CC, InternetAddress(it)) }

        bcc.forEach { message.addRecipient(Message.RecipientType.BCC, InternetAddress(it)) }

        return message
    }

    private fun smtpSession() : Session {

        val session = Session.getInstance(propSmtp(), authenticator() )

        session.debug = false

        return session
    }

    private fun authenticator(): Authenticator? {
        if(!smtpProperties.isAuth) {
            return null
        }

        LoggerFactory.getLogger(SendMail::class.java)?.info("IS AUTH OK")

        return object :Authenticator() {
            override fun getPasswordAuthentication() : PasswordAuthentication =
                    PasswordAuthentication(smtpProperties.user, smtpProperties.password)
        }
    }

    @Throws(MessagingException::class)
    fun Session.messageSend(message :MimeMessage) {

        val transport = getTransport("smtp")

        transport.connect(smtpProperties.host, smtpProperties.user, smtpProperties.password)

        LoggerFactory.getLogger(SendMail::class.java)?.info("host=${smtpProperties.host} " +
                "user==${smtpProperties.user} password==${smtpProperties.password}")

        LoggerFactory.getLogger(SendMail::class.java)?.info("transport.connected= ${this.transport.isConnected}")


        transport.sendMessage(message, message.allRecipients)

        transport.close()
    }

    @Throws(UnsupportedEncodingException::class, MessagingException::class)
    private fun MimeMessage.addPartsMessage(body :String, attachments :Array<File>, subtypeBody: String = "plain") {
        if(attachments.isEmpty()) {
            this.setText(body, smtpProperties.charsetBody, subtypeBody)
            return
        }

        val multipart = MimeMultipart("mixed")
        val textBodyPart = MimeBodyPart()
        textBodyPart.setContent(body, "text/$subtypeBody; charset=utf-8")
        multipart.addBodyPart(textBodyPart)

        attachments.forEach {
            val attachPart = MimeBodyPart()
            attachPart.dataHandler = DataHandler( FileDataSource(it) )

            attachPart.fileName = MimeUtility.encodeText(it.name, "utf-8", "Q")

            attachPart.setHeader("Content-Type",
                    "application/octet-stream; name=" + MimeUtility.encodeText(it.name, "utf-8", "Q"))
            attachPart.setHeader("Content-Disposition",
                    "attachment; filename=" + MimeUtility.encodeText(it.name, "utf-8", "Q"))

            multipart.addBodyPart(attachPart)
        }

        this.setContent(multipart)
    }
}

