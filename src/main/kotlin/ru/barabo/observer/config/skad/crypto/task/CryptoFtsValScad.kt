package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.LocalTime

object CryptoFtsValScad : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf( FileFinderData({ File(PATH_FROM) }) )

    private const val PATH_FROM = "X:\\VAL\\FTS\\KA_FTS\\in"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS, isDuplicateName = true,
            workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "Зашифровать Fts/Val Scad"

    override fun config(): ConfigTask = ScadConfig

    private fun pathToCrypto() = "X:\\VAL\\FTS\\KA_FTS\\out".byFolderExists().absolutePath

    override fun processFile(file: File) {
        val destination = File("${pathToCrypto()}\\${file.name}")

        ScadComplex.cryptoOnlyFtsVal(file, destination, isDelSource = true)
    }
}