package ru.barabo.observer.mail.smtp

import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.TaskMapper
import ru.barabo.smtp.SendMail
import ru.barabo.smtp.SmtpProperties
import java.io.File

object BaraboSmtp : SendMail {
    override val smtpProperties: SmtpProperties = SmtpProperties(host ="ns.ptkb.ru", user = "debara",
            password = "Sn909957", from = "debara@ptkb.ru")

    val YA = arrayOf(smtpProperties.from)

    val OPER = arrayOf("oper@ptkb.ru")

    val OPER_YA = arrayOf("oper@ptkb.ru", smtpProperties.from)

    val PRIM_AUTO = arrayOf("oper@ptkb.ru", smtpProperties.from, "neganova@ptkb.ru", "brykina@ptkb.ru").onlyAfinaOrYa()

    val BOOKER = arrayOf("buh1@ptkb.ru", "kudryavceva@ptkb.ru").onlyAfinaOrYa()

    val AUTO = arrayOf("auto@ptkb.ru").onlyAfinaOrYa()

    val DOPIKI = arrayOf("dedyaeva@ptkb.ru", "shabot@ptkb.ru", "kovshova@ptkb.ru", "stepnova@ptkb.ru", "derevenskih@ptkb.ru").onlyAfina()

    val PODFT = arrayOf("podft@ptkb.ru").onlyAfina()

    val CHECKER_550P =  arrayOf("oper@ptkb.ru", "nazarov@ptkb.ru", "neganova@ptkb.ru", "dummy@ptkb.ru").onlyAfina()

    val CREDIT = arrayOf("kred@ptkb.ru").onlyAfina()

    val CHECKER_390P = arrayOf("zubar@ptkb.ru", "konyaeva@ptkb.ru", "okina@ptkb.ru").onlyAfina()

    val MANAGERS_UOD = arrayOf("farahova@ptkb.ru", "konyaeva@ptkb.ru", "okina@ptkb.ru").onlyAfina()

    private val MANAGERS_AUTO = arrayOf("sherbo@ptkb.ru", "dummy@ptkb.ru", "neganova@ptkb.ru").onlyAfina()

    val TTS = arrayOf("tts@ptkb.ru").onlyAfina()

    val CHECKER_PLASTIC = arrayOf("dummy@ptkb.ru", "oper@ptkb.ru", "neganova@ptkb.ru").onlyAfina()

    val DELB_PLASTIC = arrayOf("cards@ptkb.ru", "plastik@ptkb.ru").onlyAfina()

    private val SB_TTS = arrayOf("albert@ptkb.ru", "tts@ptkb.ru").onlyAfina()

    private val REMART_BCC = arrayOf("oper@ptkb.ru")

    private val REMART_GROUP = arrayOf("sima@ptkb.ru", "secretar@ptkb.ru", "rodionova@ptkb.ru",
            "zdorovec@ptkb.ru", "koleev@ptkb.ru").onlyAfina()

    private fun Array<String>.onlyAfina() = if(TaskMapper.isAfinaBase()) this else emptyArray()

    private fun Array<String>.onlyAfinaOrYa() = if(TaskMapper.isAfinaBase()) this else YA

    fun errorSend(error :String, subject :String, to :Array<String> = AUTO) {

        sendStubThrows(to = to, subject = subject, body = error)
    }

    private fun errorSubjectElem(elem :Elem) = "Ошибка ${elem.task?.config()?.name()}"

    private fun errorBodyElem(elem :Elem) = "Возникли ошибки в:\n" +
            "Конфиг:\t${elem.task?.config()?.name()}\n" +
            "Задача:\t${elem.task?.name()}\n" +
            "Имя:\t${elem.name}\n" +
            "Id:\t${elem.idElem}\n" +
            "Path:\t${elem.path}\n" +
            "Создан:\t${elem.created}\n" +
            "Обработан:\t${elem.executed}\n" +
            "База :\t${baseName(elem.base)}\n" +
            "Ошибка:\t${elem.error}"

    private fun baseName(base :Int) = if(base == 1) "AFINA" else "TEST"


    fun errorSend(elem :Elem) {

        sendStubThrows(to = AUTO, subject = errorSubjectElem(elem), body = errorBodyElem(elem))
    }

    private val suspendElems = ArrayList<Elem>()

    fun addSuspendElem(elem :Elem) {

        synchronized(suspendElems) {
            suspendElems.add(elem)
        }
    }

    private const val REMART_SUBJECT = "По ремарту"

    private const val REMART_BODY = "From:05guprim@vladivostok.cbr.ru"

    fun sentByRemart(file : File) {

        send(to = REMART_GROUP, bcc = REMART_BCC, subject = REMART_SUBJECT, body = REMART_BODY, attachments = arrayOf(file))
    }

    private const val OTK_SUBJECT = "From 05otk@vladivostok.cbr.ru"

    fun sendToTtsFromOtk(file : File) {

        send(to = TTS, bcc = REMART_BCC, subject = OTK_SUBJECT, body = " ", attachments = arrayOf(file))
    }

    private const val OTK_UNKNOWN_SUBJECT = "Возможно важное вложение от 05otk@vladivostok.cbr.ru"

    private const val OTK_UNKNOWN_BODY =
            "Внимание, это вложение не было распознано как информация о справочниках и/или классификаторах.\n" +
                    "Возможно это важное вложение от 05otk@vladivostok.cbr.ru"

    fun sendToTtsUnknownFile(file : File) {

        send(to = MANAGERS_AUTO, cc = SB_TTS, bcc = REMART_BCC, subject = OTK_UNKNOWN_SUBJECT, body = OTK_UNKNOWN_BODY,
                attachments = arrayOf(file))
    }

    private const val CBR_UNKNOWN_SUBJECT = "Возможно важное вложение от 05NikitaTI@vladivostok.cbr.ru"

    fun sendToSbFromCbr(file : File) {

        send(to = MANAGERS_AUTO, cc = SB_TTS, bcc = REMART_BCC, subject = CBR_UNKNOWN_SUBJECT, body = CBR_UNKNOWN_SUBJECT,
                attachments = arrayOf(file))
    }
}