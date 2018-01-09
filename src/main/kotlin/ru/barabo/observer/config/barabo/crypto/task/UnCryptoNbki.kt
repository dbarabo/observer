package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.CryptoPro
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UnCryptoNbki : FileFinder, FileProcessor {

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(CryptoNbki::cryptoNbki,"K301BB000001.*\\.p7m"))

    override fun name(): String = "НБКИ Расшифровать"

    override fun config(): ConfigTask = CryptoConfig

    private fun unCryptoFolder(file: File) : File {
        val folder = File("${file.parent}/UNCRYPTO")

        if(!folder.exists()) {
            folder.mkdirs()
        }

        return folder
    }

    private val EXEC_CHECK_SEND = "{call od.PTKB_NBKI.checkSendAll}"

    override fun processFile(file: File) {

        val unCryptoFolder = unCryptoFolder(file)

        val decodeFile = File("${unCryptoFolder.absolutePath}/${file.name}")

        file.copyTo(decodeFile, true)

        val zipFile = File("${unCryptoFolder.absolutePath}/${decodeFile.nameWithoutExtension}")

        CryptoPro.decode(decodeFile, zipFile)

        Archive.extractFromCab(zipFile, unCryptoFolder.absolutePath)
    }
}