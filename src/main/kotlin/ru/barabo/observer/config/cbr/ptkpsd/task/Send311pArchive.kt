package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.task.SaveAccount311p
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Send311pArchive : SingleSelector {

    override val select: String = "select id, FILE_NAME from od.ptkb_361p_archive where state = 0 and sysdate > created + 1/24"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(9, 0),
            workTimeTo = LocalTime.of(18, 0), executeWait = Duration.ofSeconds(10))

    override fun name(): String = "311-П Отправка архива"

    override fun config(): ConfigTask = PtkPsd

    override fun execute(elem: Elem): State {
        val idArchive = elem.idElem  ?: throw SessionException("idElem is null")

        val isPhysic = selectValueType<Number>(SELECT_TYPE_ARCHIVE, arrayOf(idArchive))?.toInt() != 0

        val archive = File("${SaveAccount311p.cryptoFolder(isPhysic)}/${elem.name}.ARJ")

        val sessionSetting  = AfinaQuery.uniqueSession()

        AfinaQuery.execute(EXEC_SEND_ARCHIVE, arrayOf(idArchive), sessionSetting)

        try {
            if(!archive.exists()) throw SessionException("file not found ${archive.absolutePath}")

            SendByPtkPsdCopy.executeFile(archive)
        } catch (e: Exception) {

            AfinaQuery.rollbackFree(sessionSetting)

            throw SessionException(e.message?:"")
        }

        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }

    private const val EXEC_SEND_ARCHIVE = "{ call od.PTKB_440P.sendArchive311p( ? ) }"

    private const val SELECT_TYPE_ARCHIVE = "select IS_PHYSIC from od.ptkb_361p_archive where id = ?"
}