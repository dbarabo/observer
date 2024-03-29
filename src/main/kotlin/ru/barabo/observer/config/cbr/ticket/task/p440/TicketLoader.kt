package ru.barabo.observer.config.cbr.ticket.task.p440

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.getFolder440p
import ru.barabo.observer.config.task.p440.load.XmlLoader
import ru.barabo.observer.config.task.p440.load.xml.ticket.AbstractTicket
import ru.barabo.observer.config.task.p440.load.xml.ticket.TicketInfo
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File

private val logger = LoggerFactory.getLogger(TicketLoader::class.java)

abstract class TicketLoader<T> : FileProcessor where T : AbstractTicket {

    companion object {
        private const val INSERT_TICKET = "{ call od.PTKB_440P.insertTicket(?, ?, ?, ?, ?, ?, ?) }"

        private const val SUBJECT_440P_ERROR = "440-П Ошибка в квитанциях"

        private const val SOURCE_FOLDER = "src"
    }

    override fun processFile(file: File) {

        val folderTo = "${file.parent}/$SOURCE_FOLDER".byFolderExists()

        try {
            ScadComplex.unsignAndMoveSource(file, folderTo)
        } catch (e: Exception) {
            logger.error("ScadComplex.unsignAndMoveSource file=$file", e)
        }

        val info = try {
          XmlLoader<T>().load(file).ticketInfo
        } catch (e: Exception) {
            logger.error("XmlLoader<T>().load=$file this=$this", e)
            throw Exception(e)
        }

        val params :Array<Any?> = arrayOf(info.resposeFileName.substringBeforeLast("."),
                file.nameWithoutExtension, info.dateTimeTicket,
                info.codes?.joinToString(";") ?: String::class.javaObjectType,
                info.errorDescriptions?.joinToString(";") ?: String::class.javaObjectType,
                info.errorValues?.joinToString(";") ?: String::class.javaObjectType,
                info.errorAtributes?.joinToString(";") ?: String::class.javaObjectType)

        AfinaQuery.execute(INSERT_TICKET, params)

        val fileTo = File("${folderLoaded440p().absolutePath}/${file.name}")

        file.copyTo(fileTo, true)
        file.delete()

        if(info.isFailTicketInfo()) {

            processFailInfo(info, file)
        }
    }

    protected open fun processFailInfo(info :TicketInfo, file :File) {

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_440P_ERROR, body = info.errorText(file) )
    }

}

fun folderLoaded440p() :File = "${getFolder440p().absolutePath}/loaded".byFolderExists()

private fun TicketInfo.isFailTicketInfo() :Boolean = this.codes?.firstOrNull { "01" != it } != null

private fun TicketInfo.errorText(file :File) =
        "Ошибка в квитке\n" +
                "\tФайл квитка: ${file.name}\n" +
                "\tФайл с ошибкой: $resposeFileName\n" +
                "\tДата квитанции: $dateTimeTicket\n" +
                "\tКоды ошибок:${codes?.joinToString(";")}\n" +
                "\tОписание ошибок:${errorDescriptions?.joinToString(";")}\n" +
                "\tОшибочные атрибуты:${errorAtributes?.joinToString(";")}\n" +
                "\tОшибочные значения:${errorValues?.joinToString(";")}\n"