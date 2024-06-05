package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.skad.plastic.task.LoaderNbkiFileSent
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.CryptoPro
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UnCryptoNbki : FileFinder, FileProcessor {

    override fun name(): String = "НБКИ Расшифровать"

    override fun config(): ConfigTask = CryptoConfig

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(CryptoNbki::cryptoNbki,"K301BB000001.*\\.p7m"))

    override fun processFile(file: File) {

        val unCryptoFolder = unCryptoFolder(file)

        val decodeFile = File("${unCryptoFolder.absolutePath}/${file.name}")

        file.copyTo(decodeFile, true)

        val zipFile = File("${unCryptoFolder.absolutePath}/${decodeFile.nameWithoutExtension}")

        CryptoPro.decode(decodeFile, zipFile)

        Archive.extractFromCab(zipFile, unCryptoFolder.absolutePath)

        //checkTicket(unCryptoFolder, zipFile.nameWithoutExtension)
        checkTicketRutDf(unCryptoFolder, zipFile.nameWithoutExtension)
    }

    private fun unCryptoFolder(file: File) : File {
        val folder = File("${file.parent}/UNCRYPTO")

        if(!folder.exists()) {
            folder.mkdirs()
        }

        return folder
    }
}

private fun checkTicketRutDf(unCryptoFolder: File, ticketFileName: String) {
    val ticketFile = File("${unCryptoFolder.absoluteFile}/$ticketFileName")
    if(!ticketFile.exists()) throw Exception ("Не найден файл квитанции $ticketFile")

    if(ticketFileName.indexOf("_ticket1") > 0) {
        checkTicketSignCheck(ticketFile)
    } else {
        checkTicketRejectFile(ticketFile)
    }
}

private fun checkTicketRejectFile(ticket: File) {

    val rejectFileName = ticket.readLines()
        .firstOrNull { it.indexOf(REJECT_FILE) == 0 }
        ?.substringAfter(REJECT_FILE)?.trim()
        ?: throw Exception ("В файле квитанции $ticket не найдена запись $REJECT_FILE")

    val sendFileName = ticket.name.substringBeforeLast("_ticket")

    AfinaQuery.execute(EXEC_SAVE_TICKET2, arrayOf(sendFileName, ticket.absolutePath, rejectFileName))

    if(rejectFileName.isEmpty()) {
        loadFileWithBody(ticket)
        return
    }

    val rejectFile = File("${ticket.parent}/$rejectFileName")

    createMantisError(rejectFile)
}

private fun loadFileWithBody(ticket: File) {

    val fileNameLoad = ticket.name.substringBeforeLast("_ticket")

    LoaderNbkiFileSent.loadByFile(File(fileNameLoad) )
}

private fun checkTicketSignCheck(ticketSign: File) {

    val types = listOf(DECRIPTION_RESULT, EXTRACT_RESULT, SIGNATURE_RESULT)

    for (type in types) {
        val result = ticketSign.readLines()
            .firstOrNull { it.indexOf(type) == 0 }
            ?.substringAfter(type)?.trim()?.uppercase()
            ?: throw Exception ("В файле квитанции $ticketSign не найдена запись $type")

        if(result != "OK") throw Exception ("В файле квитанции $ticketSign ошибка типа $type error = $result")
    }
}

private fun checkTicket(unCryptoFolder: File, ticketFileName: String) {
    val ticketFile = File("${unCryptoFolder.absoluteFile}/$ticketFileName")
    if(!ticketFile.exists()) throw Exception ("Не найден файл квитанции $ticketFile")

    val lineRedjectCount = ticketFile.readLines().firstOrNull { it.indexOf(REJECTS_RECORDS) == 0 }
        ?: throw Exception ("В файле квитанции $ticketFile не найдена запись $REJECTS_RECORDS")

    val count = lineRedjectCount.substringAfter(REJECTS_RECORDS).trim().toIntOrNull()
        ?: throw Exception ("В файле квитанции $ticketFile в строке $lineRedjectCount не распознано кол-во")

    if(count != 0) {

        val rejectFileName = "${ticketFileName.substringBeforeLast("_ticket")}_reject"

        val rejectFile = File("${unCryptoFolder.absoluteFile}/$rejectFileName")

        createMantisError(rejectFile)
    }
}

private fun createMantisError(rejectFile: File) {
    BaraboSmtp.sendMantisTicket(subjectError(), rejectFile)
}

private fun subjectError() = "Квиток от НБКИ с ошибками"

private const val REJECTS_RECORDS = "RejectedRecords\t"

private const val DECRIPTION_RESULT = "Decryption result\t"

private const val EXTRACT_RESULT = "Extract result\t"

private const val SIGNATURE_RESULT = "Signature check result\t"

private const val REJECT_FILE = "RejectFile\t"

private const val EXEC_SAVE_TICKET2 = "{ call od.PTKB_RUTDF.saveTicket2(?, ?, ?) }"