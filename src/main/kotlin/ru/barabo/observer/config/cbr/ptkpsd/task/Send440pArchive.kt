package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Send440pArchive :SingleSelector {

    override val select: String = "select id, FILE_NAME from od.ptkb_440p_archive where state = 3"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
            workTimeTo = LocalTime.of(18, 0), executeWait = Duration.ofSeconds(5))

    override fun name(): String = "440-П Отправка архива"

    override fun config(): ConfigTask = PtkPsd

    private val EXEC_SEND_ARCHIVE = "call od.PTKB_440P.execSendArchive(?, ?)"

    private fun sendFolder440p() :String = "X:/440-П/${Get440pFiles.todayFolder()}/Отправка/crypto"

    override fun execute(elem: Elem): State {

        if(elem.idElem == null) throw SessionException("idElem is null")

        val sessionSetting  = AfinaQuery.uniqueSession()

        AfinaQuery.execute(
                query = EXEC_SEND_ARCHIVE,
                sessionSetting = sessionSetting,
                params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(java.sql.Types.VARCHAR) )

        val archive = File("${sendFolder440p()}/${elem.name}.ARJ")

        try {
            if(!archive.exists()) {
                throw SessionException("file not found ${archive.absolutePath}")
            }

            SendByPtkPsdCopy.executeFile(archive)
        } catch (e :Exception) {

            AfinaQuery.rollbackFree(sessionSetting)

            throw SessionException(e.message?:"")
        }
        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }
}