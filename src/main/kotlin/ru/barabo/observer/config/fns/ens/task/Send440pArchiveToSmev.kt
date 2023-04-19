package ru.barabo.observer.config.fns.ens.task

import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.SMEV_CHECK
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.cbr.ptkpsd.task.SendByPtkPsdCopy
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

    override val select: String = "select id, FILE_NAME from od.ptkb_440p_archive where state = 3 and coalesce(IS_SMEV_SOURCE, 0) = 1"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
        workTimeTo = LocalTime.of(18, 0), executeWait = Duration.ofSeconds(5))

    override fun name(): String = "ЕНС Отправка архива для Смев"

    override fun config(): ConfigTask = EnsConfig //PtkPsd

    private const val EXEC_SEND_ARCHIVE = "call od.PTKB_440P.execSendArchive(?, ?)"

    override fun execute(elem: Elem): State {

        if(elem.idElem == null) throw SessionException("idElem is null")

        val sessionSetting  = AfinaQuery.uniqueSession()

        AfinaQuery.execute(
            query = EXEC_SEND_ARCHIVE,
            sessionSetting = sessionSetting,
            params = arrayOf(elem.idElem),
            outParamTypes = intArrayOf(java.sql.Types.VARCHAR) )

        val archivePath = getArchivePath(true)

        val archive = File("${archivePath}/${elem.name}.ARJ")

        try {
            if(!archive.exists()) {
                throw SessionException("file not found ${archive.absolutePath}")
            }

            val zipArhive = "$archivePath/${elem.name}.zip"

            val zipFile = Archive.packToZip(zipArhive, archive)

            val outFile = File("$SMEV_OUT/${zipFile.name}")

            zipFile.copyTo(outFile, true)

            if(!outFile.exists()) {
                throw Exception ("Не удалось скопировать zip-файл $zipFile")
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

val SMEV_OUT: String = "$X440P/$SMEV_CHECK/out"