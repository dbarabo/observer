package ru.barabo.observer.config.skad.crypto.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.p440.SMEV_CHECK
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator.Companion.sendFolder440p
import ru.barabo.observer.config.barabo.p440.out.sendFolder440pSmev
import ru.barabo.observer.config.barabo.p440.task.AddToArchive440p
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.cbr.ticket.task.NEW_SMEV_440P_FOLDER
import ru.barabo.observer.config.cbr.ticket.task.NEW_SMEV_UNO403_FOLDER
import ru.barabo.observer.config.cbr.ticket.task.sentFolderNewSmev440pToday
import ru.barabo.observer.config.cbr.ticket.task.sentFolderNewSmevUno403Today
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.LocalTime
import java.util.regex.Pattern

object AddToArchive440pScad : FileFinder, FileProcessor {

    override fun name(): String = "Добавить файл в архив 440-П"

    override fun config(): ConfigTask = EnsConfig

    private val logger = LoggerFactory.getLogger(AddToArchive440p::class.java)

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(::sourceFolder, "PB\\d.*\\.xml"),
            FileFinderData(::sourceFolderSmev, "PB\\d.*\\.xml"),

            FileFinderData(Send440pArchive::sendFolderCrypto440p, "B(VD|VS|NS|NP|OS).*\\.vrb")
    )

    override val accessibleData: AccessibleData = AccessibleData(workTimeTo = LocalTime.of(18, 30))

    private val PATTERN_PB =  Pattern.compile("PB\\d.*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

    private const val EXEC_ADD_TO_ARCHIVE = "{ call od.PTKB_440P.setToArchiveFile(?, ?) }"

    override fun processFile(file: File) {

        val isSmevFile = file.parent.indexOf(SMEV_CHECK, ignoreCase = true) >= 0

        val processFile =  getFullPathProcessFile(file, isSmevFile)

        val settingSession = AfinaQuery.uniqueSession()

        try {
            val archive = AfinaQuery.execute(EXEC_ADD_TO_ARCHIVE, arrayOf(processFile.nameWithoutExtension),
                    settingSession, intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

            val archivePath = getArchivePath(archive, isSmevFile)

            val archiveFullPath = "$archivePath/$archive"

            addToArchive(archiveFullPath, processFile)

            AfinaQuery.commitFree(settingSession)
        } catch (e :Exception) {

            logger.error("processFile $processFile", e)

            AfinaQuery.rollbackFree(settingSession)

            throw Exception(e.message)
        }
    }

    private fun addToArchive(archiveFullPath: String, processFile: File) {
        if(File(archiveFullPath).name.substring(0..2) == "AFN" ) {
            Archive.addToArj(archiveFullPath, arrayOf(processFile))
        } else {
            Archive.addToZip("$archiveFullPath.zip", processFile)
        }
    }

    private fun getFullPathProcessFile(file: File, isSmevFile: Boolean): File {
        return when {
            PATTERN_PB.isFind(file.name) ->

                if(file.parentFile.name in arrayOf(NEW_SMEV_440P_FOLDER, NEW_SMEV_UNO403_FOLDER))
                    file
                else
                    File(if(isSmevFile) "${sendFolder440pSmev().absolutePath}/${file.name}"
                    else "${sendFolder440p()}/${file.name}")

            else -> file
        }
    }
}

fun getArchivePath(archiveName: String, isSmevFile: Boolean): String {
    return when(archiveName.substring(0..2)) {
        "AFN" -> {
            if(isSmevFile) sendFolder440pSmev().absolutePath
            else Send440pArchive.sendFolderCrypto440p().absolutePath
        }

        "BNK" -> {
            sentFolderNewSmev440pToday().absolutePath
        }

        "UBS" -> {
            sentFolderNewSmevUno403Today().absolutePath
        }

        else -> throw Exception("unknown archive type $archiveName")
    }
}
