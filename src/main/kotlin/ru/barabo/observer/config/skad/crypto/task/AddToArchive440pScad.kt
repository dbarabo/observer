package ru.barabo.observer.config.skad.crypto.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator.Companion.sendFolder440p
import ru.barabo.observer.config.barabo.p440.task.AddToArchive440p
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.skad.crypto.task.PbSaverScad.sourceFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.util.regex.Pattern

object AddToArchive440pScad : FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(AddToArchive440p::class.java)

    override fun name(): String = "Добавить Scad в архив 440-П"

    override fun config(): ConfigTask = ScadConfig

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(::sourceFolder, "PB\\d.*\\.xml")
    //        FileFinderData(Send440pArchive::sendFolderCrypto440p, "B(VD|VS|NS|NP|OS).*\\.vrb")
    )

    override val accessibleData: AccessibleData = AccessibleData()

    private val PATTERN_PB =  Pattern.compile("PB\\d.*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

    private const val EXEC_ADD_TO_ARCHIVE = "{ call od.PTKB_440P.setToArchiveFile(?, ?) }"

    override fun processFile(file: File) {

        val processFile =  if(PATTERN_PB.isFind(file.name) ) File("${sendFolder440p()}/${file.name}") else file

        val settingSession = AfinaQuery.uniqueSession()

        try {
            val archive = AfinaQuery.execute(EXEC_ADD_TO_ARCHIVE, arrayOf(processFile.nameWithoutExtension),
                    settingSession, intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

            val archiveFullPath = "${Send440pArchive.sendFolderCrypto440p().absolutePath}/$archive"

            Archive.addToArj(archiveFullPath, arrayOf(processFile))

            AfinaQuery.commitFree(settingSession)
        } catch (e :Exception) {

            logger.error("processFile $processFile", e)

            AfinaQuery.rollbackFree(settingSession)

            throw Exception(e.message)
        }
    }
}