package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.CertificateType
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.LocalTime

object CryptoFtsValScad : CryptoInOut("X:\\VAL\\FTS\\KA_FTS", CertificateType.FTS_VAL) {
    override fun name(): String = "Зашифровать In/Out Scad FTS"
}

object CryptoValCb181UScad : CryptoInOut("X:\\VAL\\CB\\KA_CB", CertificateType.FTS_CB181U) {
    override fun name(): String = "Зашифровать In/Out Scad Val ЦБ 181-У"
}

open class CryptoInOut(private val path: String, private val certType: CertificateType) :  FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData( { File("$path\\in") } ) )

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS, isDuplicateName = true,
            workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "Зашифровать In/Out Scad"

    override fun config(): ConfigTask = ScadConfig

    override fun processFile(file: File) {
        val destination = File("$path\\out\\${file.name}")

        ScadComplex.cryptoOnlyFtsVal(file, destination, certType, isDelSource = true)
    }
}