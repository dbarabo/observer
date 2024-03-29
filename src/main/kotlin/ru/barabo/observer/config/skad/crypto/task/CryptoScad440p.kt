package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator.Companion.sendFolder440p
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime

object CryptoScad440p: FileProcessor, FileFinder {

    override fun name(): String = "Зашифровать SCAD 440-П"

    override fun config(): ConfigTask = CryptoScad // ScadConfig

    override val accessibleData: AccessibleData = AccessibleData(
            WeekAccess.ALL_DAYS,
            false,
            LocalTime.MIN,
            LocalTime.of(17, 45), // LocalTime.MAX,
            Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::sendFolder440p, "B(VD|VS|NS|NP|OS).*\\.xml"))

    override fun processFile(file: File) {

        val cryptoScad = File("${Send440pArchive.sendFolderCrypto440p().absolutePath}/${file.nameWithoutExtension}.vrb")

        ScadComplex.fullEncode440p(file, cryptoScad)
    }
}