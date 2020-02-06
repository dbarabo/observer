package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime

object AddSign600P : FileFinder, FileProcessor {

    override fun name(): String = "600-П Подписать-в Архив"

    override fun config(): ConfigTask = ScadConfig

    override val accessibleData: AccessibleData = AccessibleData( workTimeFrom = LocalTime.of(9, 0),
            workTimeTo = LocalTime.of(23, 0), executeWait = Duration.ofSeconds(1) )

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData( ::pathToday, null ) )

    override fun processFile(file: File) {

        val arjArchive = File("${pathCryptoToday().absolutePath}/${arjArchiveNameToday()}")

        ScadComplex.signAddArchive600p(file, arjArchive)
    }

    private const val path = "H:/ПОД ФТ/comita/600-П"

    fun pathToday() = File("$path/${nameDateToday()}")

    fun pathCryptoToday() = File("${pathToday()}/crypto")

    private fun arjArchiveNameToday() = "DIFM_040507717_${nameDateToday()}_01_000.ARJ"
}

