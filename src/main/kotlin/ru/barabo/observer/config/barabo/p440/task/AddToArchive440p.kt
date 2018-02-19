package ru.barabo.observer.config.barabo.p440.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator.Companion.sendFolder440p
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File

object AddToArchive440p: FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(AddToArchive440p::class.java)

    override fun name(): String = "Добавить в архив"

    override fun config(): ConfigTask = P440Config

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(::sendFolder440p, "PB\\d.*\\.xml"),
            FileFinderData(Send440pArchive::sendFolderCrypto440p, "B(VD|VS|NS|NP|OS).*\\.vrb")
    )

    override val accessibleData: AccessibleData = AccessibleData()

    private const val EXEC_ADD_TO_ARCHIVE = "{ call od.PTKB_440P.setToArchiveFile(?, ?) }"

    override fun processFile(file: File) {

        val settingSession = AfinaQuery.uniqueSession()

        try {
            val archive = AfinaQuery.execute(EXEC_ADD_TO_ARCHIVE, arrayOf(file.nameWithoutExtension),
                    settingSession, intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

            val archiveFullPath = "${Send440pArchive.sendFolderCrypto440p().absolutePath}/$archive"

            Archive.addToArj(archiveFullPath, arrayOf(file))

            AfinaQuery.commitFree(settingSession)
        } catch (e :Exception) {

            logger.error("processFile $file", e)

            AfinaQuery.rollbackFree(settingSession)

            throw Exception(e.message)
        }
    }
}