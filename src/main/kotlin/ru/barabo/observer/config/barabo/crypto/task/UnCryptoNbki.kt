package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.CryptoPro
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime
import java.util.regex.Pattern

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

        checkTicket(unCryptoFolder)
    }

    private fun unCryptoFolder(file: File) : File {
        val folder = File("${file.parent}/UNCRYPTO")

        if(!folder.exists()) {
            folder.mkdirs()
        }

        return folder
    }

    private const val EXEC_CHECK_SEND = "{call od.PTKB_NBKI.checkSendAll}"
}

private fun checkTicket(unCryptoFolder: File) {

    val patternTicket = Pattern.compile(regexTicket)

    val ticketFile = unCryptoFolder.listFiles { f -> (!f.isDirectory) && (patternTicket.isFind(f.name)) }?.firstOrNull { true }
        ?: throw Exception ("Не найден файл квитанции в папке $unCryptoFolder")

    val lineRedjectCount = ticketFile.readLines().firstOrNull { it.indexOf(REJECTS_RECORDS) == 0 }
        ?: throw Exception ("В файле квитанции $ticketFile не найдена запись $REJECTS_RECORDS")

    val count = lineRedjectCount.substringAfter(REJECTS_RECORDS).trim().toIntOrNull()
        ?: throw Exception ("В файле квитанции $ticketFile в строке $lineRedjectCount не распознано кол-во")

    if(count != 0) createMantisError(count, unCryptoFolder)
}

private fun createMantisError(countError: Int, unCryptoFolder: File) {
    val patternReject = Pattern.compile(regexReject)

    val rejectFile = unCryptoFolder.listFiles { f -> (!f.isDirectory) && (patternReject.isFind(f.name)) }?.firstOrNull { true }
        ?: throw Exception ("Не найден файл K301BB000001_*_reject в папке $unCryptoFolder")

    BaraboSmtp.sendMantisTicket(subjectError(countError), rejectFile)
}

private fun subjectError(count: Int) = "Квиток от НБКИ с ошибками. Ошибок:$count"

private const val regexTicket = "K301BB000001_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\d_ticket"

private const val regexReject = "K301BB000001_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\d_reject"

private const val REJECTS_RECORDS = "RejectedRecords\t"