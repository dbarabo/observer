package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.task.Send311pArchive
import ru.barabo.observer.config.cbr.ptkpsd.task.Send311pArchive.cryptoFolder
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object SignScadArchive311p : SingleSelector {
    override val select: String = "select id, FILE_NAME from od.ptkb_361p_archive where state = 0 and created >= sysdate - 40/(60*24)"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(9, 0),
            workTimeTo = LocalTime.of(18, 0), executeWait = Duration.ofSeconds(10))

    override fun name(): String = "311-П Подписать архив scad"

    override fun config(): ConfigTask = ScadConfig

    override fun execute(elem: Elem): State {

        val idArchive = elem.idElem  ?: throw SessionException("idElem is null")

        val isPhysic = selectValueType<Number>(Send311pArchive.SELECT_TYPE_ARCHIVE, arrayOf(idArchive))?.toInt() != 0

        val archive = File("${cryptoFolder(isPhysic)}/${elem.name}.ARJ")

        val sessionSetting  = AfinaQuery.uniqueSession()

        try {
            AfinaQuery.execute(EXEC_SIGN_ARCHIVE, arrayOf(idArchive), sessionSetting)

            if(!archive.exists()) throw SessionException("file not found ${archive.absolutePath}")

            ScadComplex.signAndMoveSource(archive)
        } catch (e: Exception) {

            AfinaQuery.rollbackFree(sessionSetting)

            throw SessionException(e.message?:"")
        }
        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }

    private const val EXEC_SIGN_ARCHIVE = "{ call od.PTKB_440P.signArchive311p(?) }"
}