package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.task.ToUncrypto440p
import ru.barabo.observer.config.cbr.ticket.task.getFolder440p
import ru.barabo.observer.config.skad.crypto.ScadConfig
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
    override fun name(): String = "Расшифровать scad vrb-файлы 440-П"

    override fun config(): ConfigTask = ScadConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.of(16, 0)/*LocalTime.MAX*/, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::getFolder440p, ".*\\.vrb"))

    override fun processFile(file: File) {

        val decodeFile = File("${ToUncrypto440p.getUncFolder440p().absolutePath}/${file.nameWithoutExtension}.xml")

        ScadComplex.fullDecode440p(file, decodeFile)
    }
}