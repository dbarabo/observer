package ru.barabo.observer.config.skad.crypto.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object SignScadArchive440p : SingleSelector {

    private val logger = LoggerFactory.getLogger(SignScadArchive440p::class.java)

    override fun name(): String = "440-П Подписать архив scad"

    override fun config(): ConfigTask = CryptoScad // ScadConfig

    override val accessibleData: AccessibleData = AccessibleData(
            workTimeFrom = LocalTime.of(8, 0),
            workTimeTo = LocalTime.of(17, 0),
            executeWait = Duration.ofSeconds(5))

    override val select: String = "select id, FILE_NAME from od.ptkb_440p_archive where state = 0 and " +
            "trunc(created) = trunc(sysdate) and (count_files = 50 or sysdate - created > 1/(24*60))"

    override fun execute(elem: Elem): State {

        val results = AfinaQuery.execute(
                query = EXEC_SIGN_ARCHIVE,
                params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(java.sql.Types.VARCHAR, OracleTypes.NUMBER) )

        val isSmevArchive: Int = (results?.get(1) as? Number)?.toInt() ?: 0

        val archivePath = getArchivePath(isSmevArchive != 0)

        val archive = File("$archivePath/${elem.name}.ARJ")

        try {
            if(!archive.exists()) throw SessionException("file not found ${archive.absolutePath}")

            ScadComplex.signAndMoveSource(archive, sourceFolder())
        } catch (e :Exception) {
            logger.error("execute", e)

            AfinaQuery.execute(EXEC_UNSIGN_ARCHIVE, arrayOf(elem.idElem))

            throw SessionException(e.message?:"")
        }

        return State.OK
    }
}

private const val EXEC_SIGN_ARCHIVE = "{ call od.PTKB_440P.signArchiveFileAndSmev(?, ?, ?) }"

private const val EXEC_UNSIGN_ARCHIVE = "{ call od.PTKB_440P.unSignArchiveFile(?) }"

