package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.skad.crypto.task.AddSign600P.pathCryptoTicketToday
import ru.barabo.observer.config.skad.crypto.task.AddSignMain600P.pathCryptoMainToday
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime

object CryptoArchive600P : FileFinder, FileProcessor {

    override fun name(): String = "600-П Зашифровать-отправить"

    override fun config(): ConfigTask = ScadConfig

    override val accessibleData: AccessibleData = AccessibleData( workTimeFrom = LocalTime.of(9, 0),
            workTimeTo = LocalTime.of(23, 30), executeWait = Duration.ofMinutes(2) )

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData( ::pathCryptoTicketToday, "ARHKRFM_040507717_.*\\.ARJ" ),
            FileFinderData( ::pathCryptoMainToday, "DIFM_040507717_.*\\.ARJ" ) )

    override fun processFile(file: File) {

        val signArchive = ScadComplex.cryptoArchive600p(file, pathArchiveSrcToday(file) )

        val copyOut = File("${SignScadSend550p.ptkPsdOutPath}/${signArchive.name}")

        signArchive.copyTo(copyOut, true)
    }

    private fun pathArchiveSrcToday(file: File) = "${file.parentFile.parentFile.path}/src".byFolderExists()
            //"${AddSign600P.pathTicketToday()}/src".byFolderExists()
}