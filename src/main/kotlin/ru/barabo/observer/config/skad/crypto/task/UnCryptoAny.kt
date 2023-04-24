package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.fns.scad.CryptoScad
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

object UnCryptoAny  : FileFinder, FileProcessor {

    override fun name(): String = "Расшифровать-ЭЦП XML-C:/VAL/CB/decrypt"

    override fun config(): ConfigTask = CryptoScad // ScadConfig

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(7, 0), LocalTime.of(23, 0), Duration.ZERO)

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::unCryptoValCbDecrypt,".*\\.xml"))

    private val X_VAL_CB_DECRYPT = if(TaskMapper.isAfinaBase())"X:/VAL/CB/decrypt" else "C:/VAL/CB/decrypt"

    private fun unCryptoValCbDecrypt(): File = "$X_VAL_CB_DECRYPT/in".byFolderExists()

    override fun processFile(file: File) {

        val uncryptFile = File("$X_VAL_CB_DECRYPT/out/${file.name}")

        ScadComplex.fullDecode364p(file, uncryptFile)

        if(!uncryptFile.exists()) throw Exception("decode file not found ${file.absolutePath}")

        file.delete()
    }
}