package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.config.ConfigTask
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

object Sign390pArchive : FileFinder, FileProcessor {

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(8, 0),
            LocalTime.of(20, 0), Duration.ofMinutes(6))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(CreateSaveResponse390p::sendFolder390p,"AFT_0507717.*\\.ARJ") )

    override fun name(): String = "390-П Подписать архив"

    override fun config(): ConfigTask = ScadConfig // CryptoConfig

    override fun processFile(file: File) {
        // Verba.signBy390p(file)
        ScadComplex.signAndMoveSource(file, CreateSaveResponse390p.sendFolderSrc390p())
    }
}