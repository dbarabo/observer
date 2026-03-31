package ru.barabo.observer.config.fns.ens.task

import oracle.jdbc.OracleTypes
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object SendArchive311pZip : SingleSelector {
    override fun name(): String = "311-П Отправить архив smev"

    override fun config(): ConfigTask = EnsConfig

    override val select: String = "select id, FILE_NAME from od.ptkb_361p_archive where state = 0 and created >= sysdate - 50/(60*24)"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(9, 0),
        workTimeTo = LocalTime.of(17, 55), executeWait = Duration.ofSeconds(20))

    override fun execute(elem: Elem): State {

        val idArchive = elem.idElem  ?: throw SessionException("idElem is null")

        val isFns = selectValueType<Number>(SELECT_ARCHIVE_IS_FNS, arrayOf(idArchive))?.toInt() != 0

        val archiveNameNoExt = elem.name

        val (folderSmev, folderNow) = folderSmevByIsFns(isFns)

        val sessionSetting  = AfinaQuery.uniqueSession()
        try {
            AfinaQuery.execute(query = EXEC_SEND_ARCHIVE_ZIP, params = arrayOf(idArchive),
                sessionSetting, outParamTypes = intArrayOf(OracleTypes.VARCHAR))

            val archiveFullPath = File("${folderNow.absolutePath}/$archiveNameNoExt.zip")

            if(!archiveFullPath.exists()) throw SessionException("file not found ${archiveFullPath.absolutePath}")

            val archiveSmev = File("$folderSmev/$archiveNameNoExt.zip")

            archiveFullPath.copyTo(archiveSmev, overwrite = true)
        } catch (e: Exception) {

            AfinaQuery.rollbackFree(sessionSetting)

            throw SessionException(e.message?:"")
        }
        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }

    private fun folderSmevByIsFns(isFns: Boolean): Pair<String, File> {

        return if(isFns) {
            Pair(FNS_TO_SMEV, fnsFolderSent())
        } else {
            Pair(SFR_TO_SMEV, sfrFolderSent())
        }
    }
}

private const val SELECT_ARCHIVE_IS_FNS = "select is_fns from od.ptkb_361p_archive where id = ?"

private const val EXEC_SEND_ARCHIVE_ZIP = "{ call od.PTKB_440P.sendArchive311pZip(?) }"