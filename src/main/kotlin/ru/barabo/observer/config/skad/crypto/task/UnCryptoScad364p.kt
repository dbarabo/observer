package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UnCryptoScad364p : FileFinder, FileProcessor {

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(::unCrypto364p,"DT10......_......_......._._0021_0000_............\\.xml"))

    override fun name(): String = "364-П Scad Расшифровать+ЭЦП DT"

    override fun config(): ConfigTask = ScadConfig

    private val X364P = if(TaskMapper.isAfinaBase())"X:/364-П" else "C:/364-П"

    fun unCrypto364p(): File = "$X364P/${Get440pFiles.todayFolder()}".byFolderExists()

    override fun processFile(file: File) {

        ScadComplex.fullDecode364p(file, file)

        if(!file.exists()) throw Exception("decode file not found ${file.absolutePath}")
    }
}