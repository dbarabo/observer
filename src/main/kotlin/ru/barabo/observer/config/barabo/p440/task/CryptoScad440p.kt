package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
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

    override fun name(): String = "Зашифровать SCAD"

    override fun config(): ConfigTask = P440Config

    override val accessibleData: AccessibleData = AccessibleData(
            WeekAccess.ALL_DAYS,
            false,
            LocalTime.MIN,
            LocalTime.MAX,
            Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::sendScadFolder440p, "B(VD|VS|NS|NP|OS).*\\.xml"))

    override fun processFile(file: File) {

        val cryptoScad = File("${sendCryptoScadFolder440p().absolutePath}/${file.nameWithoutExtension}.vrb.tst")

        ScadComplex.fullEncode440p(file, cryptoScad)
    }

    private fun sendCryptoScadFolder440p(): File = "${GeneralCreator.sendFolder440p().absoluteFile}/scadcrypto".byFolderExists()

    private fun sendScadFolder440p(): File = "${GeneralCreator.sendFolder440p().absoluteFile}/scad".byFolderExists()
}