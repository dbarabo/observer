package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.Verba
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UnSign364p : FileFinder, FileProcessor {

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(UnCrypto364p::unCrypto364p, "DT10......_......_......._._0021_0000_............\\.xml", true))

    override fun name(): String = "364-П Снять ЭЦП"

    override fun config(): ConfigTask = CryptoConfig

    override fun processFile(file: File) {

        Verba.unSignFile(file)
    }
}