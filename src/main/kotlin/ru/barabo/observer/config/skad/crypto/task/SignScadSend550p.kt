package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.skad.crypto.task.Uncrypto550pScad.folder550pOut
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime
import java.util.*

object SignScadSend550p : FileFinder, FileProcessor {

    override fun config(): ConfigTask = CryptoScad // ScadConfig

    override fun name(): String = "Подписать scad и отправить"

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( ::folder550pOut,"ARH550P_0021_0000_........_...\\.arj", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofMinutes(3))

    const val ptkPsdOutPath = "p:"

    private fun folder550pOutSrc() = "${folder550pOut().absolutePath}/src".byFolderExists()

    override fun processFile(file: File) {

        val sessionSetting = AfinaQuery.uniqueSession()

        val archiveNameNoExt = file.nameWithoutExtension.uppercase(Locale.getDefault())

        try {
            AfinaQuery.execute(EXEC_SIGNSEND_ARCHIVE, arrayOf(archiveNameNoExt), sessionSetting)

            if (!file.exists()) throw SessionException("file not found ${file.absolutePath}")

            val signedFile = ScadComplex.signAndMoveSource(file, folder550pOutSrc())

            val copyOut = File("$ptkPsdOutPath/${signedFile.name}")

            signedFile.copyTo(copyOut, true)
        } catch (e: Exception) {

            AfinaQuery.rollbackFree(sessionSetting)

            throw SessionException(e.message ?: "")
        }
        AfinaQuery.commitFree(sessionSetting)
    }

    private const val EXEC_SIGNSEND_ARCHIVE = "{ call od.PTKB_440P.sendSignArchive550p( ? ) }"
}