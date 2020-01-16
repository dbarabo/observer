package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Crypto4077UScad : FileFinder, FileProcessor {

        override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData( ::pathToday, "SKO4077U.*\\.XML" ) )

    override val accessibleData: AccessibleData = AccessibleData( workTimeFrom = LocalTime.of(6, 0) )

    override fun name(): String = "Зашифровать 4077-У Scad"

    override fun config(): ConfigTask = ScadConfig

    override fun processFile(file: File) {

        val archiveArj = ScadComplex.crypto4077U(file)

        val copyOut = File("${SignScadSend550p.ptkPsdOutPath}/${archiveArj.name}")

        archiveArj.copyTo(copyOut, true)
    }

    private const val path = "H:/ПОД ФТ/armotkaz/ExchangePoint/BANK/RFM/FM4077U_040507717_"

    private fun pathToday() = File("$path${nameDateToday()}_001")
}

fun nameDateToday() = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now())!!