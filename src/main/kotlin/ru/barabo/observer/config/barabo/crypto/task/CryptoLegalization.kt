package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.Verba
import java.io.File
import java.io.IOException
import java.time.LocalTime

object CryptoLegalization : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf( FileFinderData({ File(PATH_FROM) }) )

    private const val PATH_FROM = "H:\\Gu_cb\\207-П\\ENC"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS, isDuplicateName = true,
            workTimeFrom = LocalTime.of(5, 0))

    override fun name(): String = "Зашифровать Легализация"

    override fun config(): ConfigTask = CryptoConfig

    private fun pathToCrypto() = "H:\\Gu_cb\\207-П\\ENCRIPTED".byFolderExists().absolutePath

    override fun processFile(file: File) {
        val cryptoFile = Verba.cryptoFile(file)

        val moveFile = File("${pathToCrypto()}\\${cryptoFile.name}")

        if(!cryptoFile.renameTo(moveFile)) {
            throw IOException("file is not renamed ${cryptoFile.absolutePath}")
        }
    }
}