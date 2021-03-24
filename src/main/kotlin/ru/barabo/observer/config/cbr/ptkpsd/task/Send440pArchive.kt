package ru.barabo.observer.config.cbr.ptkpsd.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Send440pArchive : SingleSelector {

    private val logger = LoggerFactory.getLogger(Send440pArchive::class.java)

    override val select: String = "select id, FILE_NAME from od.ptkb_440p_archive where state = 3"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
            workTimeTo = LocalTime.of(18, 0), executeWait = Duration.ofSeconds(5))

    override fun name(): String = "440-П Отправка архива"

    override fun config(): ConfigTask = PtkPsd

    private const val EXEC_SEND_ARCHIVE = "call od.PTKB_440P.execSendArchive(?, ?)"

    fun sendFolderCrypto440p() :File = "${GeneralCreator.sendFolder440p().absolutePath}/crypto".byFolderExists()

    override fun execute(elem: Elem): State {

        if(elem.idElem == null) throw SessionException("idElem is null")

        val sessionSetting  = AfinaQuery.uniqueSession()

        AfinaQuery.execute(
                query = EXEC_SEND_ARCHIVE,
                sessionSetting = sessionSetting,
                params = arrayOf(elem.idElem),
                outParamTypes = intArrayOf(java.sql.Types.VARCHAR) )

        val archive = File("${sendFolderCrypto440p().absolutePath}/${elem.name}.ARJ")

        try {
            if(!archive.exists()) {
                throw SessionException("file not found ${archive.absolutePath}")
            }

            SendByPtkPsdCopy.executeFile(archive)
        } catch (e :Exception) {

            AfinaQuery.rollbackFree(sessionSetting)

            logger.error("SendByPtkPsdCopy", e)

            throw SessionException(e.message?:"")
        }
        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }
}