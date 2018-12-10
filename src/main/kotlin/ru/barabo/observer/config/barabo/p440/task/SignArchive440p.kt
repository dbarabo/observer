package ru.barabo.observer.config.barabo.p440.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.crypto.Verba
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object SignArchive440p: SingleSelector {

    private val logger = LoggerFactory.getLogger(SignArchive440p::class.java)

    override fun name(): String = "Подписать архив"

    override fun config(): ConfigTask = P440Config


    override val accessibleData: AccessibleData = AccessibleData(
            workTimeFrom = LocalTime.of(8, 0),
            workTimeTo = LocalTime.of(18, 0),
            executeWait = Duration.ofSeconds(5))

    override val select: String = "select id, FILE_NAME from od.ptkb_440p_archive where state = 0 and " +
            "trunc(created) = trunc(sysdate) and (count_files = 50 or sysdate - created > 1/24)"

    private const val EXEC_SIGN_ARCHIVE = "{ call od.PTKB_440P.signArchiveFile(?, ?) }"

    override fun execute(elem: Elem): State {

        val sessionSetting  = AfinaQuery.uniqueSession()

        AfinaQuery.execute(
                query = EXEC_SIGN_ARCHIVE,
                sessionSetting = sessionSetting,
                params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(java.sql.Types.VARCHAR) )

        val archive = File("${Send440pArchive.sendFolderCrypto440p().absolutePath}/${elem.name}.ARJ")

        try {
            if(!archive.exists()) throw SessionException("file not found ${archive.absolutePath}")

            Verba.signByBarabo(archive)
        } catch (e :Exception) {
            logger.error("execute", e)

            AfinaQuery.rollbackFree(sessionSetting)

            throw SessionException(e.message?:"")
        }

        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }
}
