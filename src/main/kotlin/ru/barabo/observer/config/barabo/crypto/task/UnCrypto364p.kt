package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.cbr.ptkpsd.task.Send364pSign
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.Verba
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UnCrypto364p : FileFinder, FileProcessor {

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(::unCrypto364p,"DT10......_......_......._._0021_0000_............\\.xml"))

    override fun name(): String = "364-П Расшифровать+ЭЦП DT"

    override fun config(): ConfigTask = CryptoConfig

    fun unCrypto364p() :File = File("X:/364-П/${Send364pSign.todayFolder()}")

    override fun processFile(file: File) {

        Verba.unCrypto(file.parentFile, file.name)

        Verba.unSignFile(file)
    }
}