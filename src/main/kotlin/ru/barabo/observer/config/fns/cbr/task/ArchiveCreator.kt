package ru.barabo.observer.config.fns.cbr.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.data.dateFormatInFile
import ru.barabo.observer.config.fns.cbr.CbrConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object ArchiveCreator : SingleSelector {

    override fun name(): String = "ЦБ-выписки в Архив"

    override fun config(): ConfigTask = CbrConfig

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
        workTimeTo = LocalTime.of(20, 0), executeWait = Duration.ofSeconds(1))

    override val select: String =
        "select id, FILENAME from od.PTKB_CBR_EXT_RESPONSE where state = 1 and ARCHIVE_ID is null"

    override fun execute(elem: Elem): State {

        val session = AfinaQuery.uniqueSession()

        return if(elem.name.uppercase().indexOf("PB") == 0) {
            createNewArchiveForPb(elem, session)
        } else {
            findOrCreateNewArchiveForExtract(elem, session)
        }
    }

    private fun findOrCreateNewArchiveForExtract(elem: Elem, session: SessionSetting): State {

        try {
            val archiveName = findOrCreateNewArchive(elem.name, session)

            val extFilename = String.format(elem.name, dateFormatInFile())

            val fileExt = File("${getCbrResponseToday().absolutePath}/$extFilename.xml")

            val archiveFile = File("${getCbrResponseToday().absolutePath}/$archiveName.zip")

            Archive.addToZip(archiveFile.absolutePath, fileExt)

            AfinaQuery.commitFree(session)

        } catch (e: Exception) {

            logger.error("createNewArchiveForPb", e)

            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }

        return State.OK
    }


    private fun createNewArchiveForPb(elem: Elem, session: SessionSetting): State {

        try {
            val archiveName = createNewArchive(elem.name, session)

            val fileExt = File("${getCbrResponseToday().absolutePath}/${elem.name}.xml")

            val archiveFile = File("${getCbrResponseToday().absolutePath}/$archiveName.zip")

            Archive.packToZip(archiveFile.absolutePath, fileExt)

            AfinaQuery.commitFree(session)

        } catch (e: Exception) {

            logger.error("createNewArchiveForPb", e)

            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }

        return State.OK
    }

    private fun createNewArchive(pbFilename: String, session: SessionSetting): String =
        AfinaQuery.execute(CALL_CREATE_ARCHIVE, arrayOf(pbFilename),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR), sessionSetting = session)?.get(0) as String

    private fun findOrCreateNewArchive(extractFilename: String, session: SessionSetting): String =
        AfinaQuery.execute(CALL_FIND_CREATE_ARCHIVE, arrayOf(extractFilename),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR), sessionSetting = session)?.get(0) as String

}

private val logger = LoggerFactory.getLogger(ArchiveCreator::class.java)

private const val CALL_CREATE_ARCHIVE = "{ call od.PTKB_CBR_EXTRACT.createNewArchive( ?, ? ) }"

private const val CALL_FIND_CREATE_ARCHIVE = "{ call od.PTKB_CBR_EXTRACT.findOrCreateNewArchive( ?, ? ) }"