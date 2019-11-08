package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UncryptoScad440p : FileProcessor, FileFinder {

    override fun name(): String = "Расшифровать SCAD"

    override fun config(): ConfigTask = P440Config

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(Get440pFiles::getFolder440p, ".*\\.vrb\\.tst"))

    fun getUncryptoScad440p() : File = "${Get440pFiles.getFolder440p().absolutePath}/unscad".byFolderExists()

    override fun processFile(file: File) {

        val unCryptoFile = File("${getUncryptoScad440p().absolutePath}/${file.nameWithoutExtension}.xml")

        ScadComplex.fullDecode440p(file, unCryptoFile)
    }
}