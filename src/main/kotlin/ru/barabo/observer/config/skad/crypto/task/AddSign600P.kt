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

    override fun name(): String = "600-П sign Квитки-в Архив"

    override fun config(): ConfigTask = ScadConfig

    override val accessibleData: AccessibleData = AccessibleData( workTimeFrom = LocalTime.of(9, 0),
            workTimeTo = LocalTime.of(23, 0), executeWait = Duration.ofSeconds(1) )

    override val fileFinderData: List<FileFinderData> = listOf( FileFinderData( ::pathTicketToday, null ) )

    override fun processFile(file: File) {

        if(file.name.equals("thumbs.db", true)) {
            file.delete()
            return
        }

        val arjArchive = File("${pathCryptoTicketToday().absolutePath}/${arjArchiveNameToday()}")

        ScadComplex.signAddArchive600p(file, arjArchive)
    }

    private fun pathTicketToday() = File("$pathTicket/${nameDateToday()}")

    fun pathCryptoTicketToday() = File("${pathTicketToday()}/crypto")

    private fun arjArchiveNameToday() = "ARHKRFM_040507717_${nameDateToday()}_001.ARJ"

    private const val pathTicket = "H:/ПОД ФТ/comita/600-П/ARHKRFM"
}

