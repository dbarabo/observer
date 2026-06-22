package ru.barabo.observer.config.fns.ens.task

import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.SMEV_CHECK
import ru.barabo.observer.config.cbr.ticket.task.SMEV_440P_OUT_FOLDER
import ru.barabo.observer.config.cbr.ticket.task.SMEV_UNO403_OUT_FOLDER
import ru.barabo.observer.config.cbr.ticket.task.X440P
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.skad.crypto.task.getArchivePath
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Send440pArchiveToSmev : SingleSelector {

    private val logger = LoggerFactory.getLogger(Send440pArchiveToSmev::class.java)

    override val select: String =
        "select id, FILE_NAME from od.ptkb_440p_archive where state = 3 and (coalesce(IS_SMEV_SOURCE, 0) = 1 or FILE_NAME not like 'AFN%')"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
        workTimeTo = LocalTime.of(18, 0), executeWait = Duration.ofSeconds(5))

    override fun name(): String = "ЕНС Отправка архива для Смев"

    override fun config(): ConfigTask = EnsConfig

    private const val EXEC_SEND_ARCHIVE = "call od.PTKB_440P.execSendArchive(?, ?)"

    override fun execute(elem: Elem): State {

        if(elem.idElem == null) throw SessionException("idElem is null")

        val sessionSetting  = AfinaQuery.uniqueSession()

        try {
            AfinaQuery.execute(
                query = EXEC_SEND_ARCHIVE,
                sessionSetting = sessionSetting,
                params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(java.sql.Types.VARCHAR) )

            val sourceZip = getZipFile(elem.name)

            val outZip = getCopyToFile(sourceZip.name)

            sourceZip.copyTo(outZip, true)

            if(!outZip.exists()) {
                throw Exception ("Не удалось скопировать zip-файл $sourceZip")
            }

        } catch (e: Exception) {

            AfinaQuery.rollbackFree(sessionSetting)

            logger.error("Send440pArchiveToSmev", e)

            throw SessionException(e.message?:"")
        }
        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }
}

private fun getCopyToFile(archiveName: String): File {

    return when(archiveName.substring(0..2)) {

        "AFN" -> {
            File("$SMEV_OUT/$archiveName")
        }

        "BNK" -> {
            File("$SMEV_440P_OUT_FOLDER/$archiveName")
        }

        "UBS"  -> {
            File("$SMEV_UNO403_OUT_FOLDER/$archiveName")
        }

        else -> throw Exception("unknown archive type $archiveName")
    }
}

private fun getZipFile(archiveNameNoExt: String): File {

    val archivePath = getArchivePath( archiveNameNoExt,true)

    return when(archiveNameNoExt.substring(0..2)) {

        "AFN" -> {

            createZipFromArj(archivePath, archiveNameNoExt)

        }

        "BNK", "UBS"  -> {
            File("$archivePath/$archiveNameNoExt.zip")
        }

        else -> throw Exception("unknown archive type $archiveNameNoExt")
    }
}

private fun createZipFromArj(archivePath: String, archiveNameNoExt: String): File {

    val arjArchive = File("${archivePath}/$archiveNameNoExt.ARJ")

    if(!arjArchive.exists()) {
        throw SessionException("file not found ${arjArchive.absolutePath}")
    }

    val zipArchive = "$archivePath/$archiveNameNoExt.zip"

    return Archive.packToZip(zipArchive, arjArchive)
}

val SMEV_OUT: String = "$X440P/$SMEV_CHECK/out"