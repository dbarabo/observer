package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles.todayFolder
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.CryptoPro
import ru.barabo.observer.mail.smtp.NbkiSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object CryptoNbki : FileFinder, FileProcessor {

    override fun name(): String = "НБКИ Зашифровать-отправить"

    override fun config(): ConfigTask = CryptoConfig

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofHours(4))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(::cryptoNbki,"K301BB000001_........_......\\.txt"))

    override fun processFile(file: File) {

        val cryptoFile = File("${cryptoFolder(file).absolutePath}/${file.nameWithoutExtension}")

        file.copyTo(cryptoFile, true)

        AfinaQuery.execute(EXEC_CHECK_SEND)

        val signedFile = File("${cryptoFile.absolutePath}.p7s")

        CryptoPro.sign(cryptoFile, signedFile)

        val zipFile =  Archive.packToZip("${cryptoFile.absolutePath}.zip", cryptoFile, signedFile)

        val encodeFile = File("${zipFile.absolutePath}.p7m")

        CryptoPro.encode(zipFile, encodeFile)

        NbkiSmtp.sendToNbki(encodeFile)
    }

    fun cryptoNbki() : File = File("X:/НБКИ/${todayFolder()}")

    private fun cryptoFolder(file: File) :File {
        val folder = File("${file.parent}/CRYPTO")

        if(!folder.exists()) {
            folder.mkdirs()
        }

        return folder
    }

    private const val EXEC_CHECK_SEND = "{call od.PTKB_NBKI.checkSendAll}"
}