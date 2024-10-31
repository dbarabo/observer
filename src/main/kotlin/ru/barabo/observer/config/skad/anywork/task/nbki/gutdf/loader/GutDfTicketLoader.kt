package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.loader

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.nbki.gutdf.ticket.MainNotificationOfAcceptance
import ru.barabo.observer.config.task.nbki.gutdf.ticket.MainNotificationOfReceipt
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File

object GutDfTicketLoader {

    fun loadReceiptFile(file: File) {

        val mainReceipt = loadReceiptFromXml(file)

        val idFile = AfinaQuery.selectValue(SEL_IDFILE_BY_NAME, params = arrayOf(mainReceipt.fileName)) as Number

        if(mainReceipt.acceptanceStatusOK == null && mainReceipt.acceptanceStatusFail != null) {
            sendFailMessage(file, idFile, mainReceipt)
            return
        }

        AfinaQuery.execute(UPDATE_FIRST_TICKET, params = arrayOf(idFile))
    }

    fun loadAcceptanceFile(file: File) {

        val mainAccept = loadAcceptFromXml(file)

        val idFile = AfinaQuery.selectValue(SEL_IDFILE_BY_NAME, params = arrayOf(mainAccept.docInfo.fileName)) as Number

        updateAcceptTicket(file, idFile)

        if(mainAccept.errors?.errorList.isNullOrEmpty() ) {
            return
        }

        processFail(file, idFile, mainAccept)
    }

    private fun updateAcceptTicket(fileTicket: File, idFile: Number) {

        AfinaQuery.execute(UPDATE_TICKET_FILE, params = arrayOf(idFile, fileTicket.absoluteFile))
    }

    private fun processFail(fileTicket: File, idFile: Number, mainAccept: MainNotificationOfAcceptance) {

        val sessionSetting = AfinaQuery.uniqueSession()

        try {

            for(error in mainAccept.errors.errorList) {

                val orderNumInFile = error.orderNum!!.value!!.toInt()

                AfinaQuery.execute(DEL_MAIN_RECORD, params = arrayOf(idFile, orderNumInFile))
            }

            AfinaQuery.commitFree(sessionSetting)
        } catch (e: Exception) {

            logger.error("processFail idFile=$idFile", e)

            AfinaQuery.rollbackFree(sessionSetting)

            sendFailAcceptMessage(fileTicket)

            throw Exception(e)
        }

        sendFailAcceptMessage(fileTicket)
    }

    private fun sendFailAcceptMessage(fileTicket: File) {

        val subject = "Ошибка НБКИ во 2-й квитанции"

        BaraboSmtp.sendMantisTicket(subject, fileTicket)
    }

    private fun sendFailMessage(fileTicket: File, idFile: Number, mainReceipt: MainNotificationOfReceipt) {

        val subject = "Ошибка НБКИ в 1-й квитанции"

        val body = "idFile=$idFile" +
            mainReceipt.errorReceiptList?.joinToString("\n") { "CODE=${it.errorCode} ERROR=${it.errorMessage}" }

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = subject, body = body, attachments = arrayOf(fileTicket) )
    }

    private fun loadReceiptFromXml(file: File): MainNotificationOfReceipt {

        val xmlLoader = XmlGutDfLoader<MainNotificationOfReceipt>()

        return xmlLoader.load(file)
    }

    private fun loadAcceptFromXml(file: File): MainNotificationOfAcceptance {

        val xmlLoader = XmlGutDfLoader<MainNotificationOfAcceptance>()

        return xmlLoader.load(file)
    }
}

private val logger = LoggerFactory.getLogger(GutDfTicketLoader::class.java)

private const val SEL_IDFILE_BY_NAME = "select OD.PTKB_RUTDF.getFileByName( ? ) from  from dual"

private const val UPDATE_FIRST_TICKET = "{ call od.PTKB_RUTDF.updateFirstTicket( ? ) }"

private const val DEL_MAIN_RECORD = "{ call od.PTKB_GUTDF.deleteRecordMain( ?, ? ) }"

private const val UPDATE_TICKET_FILE = "{ call od.PTKB_RUTDF.saveTicketAcceptReject( ?, ? ) }"
