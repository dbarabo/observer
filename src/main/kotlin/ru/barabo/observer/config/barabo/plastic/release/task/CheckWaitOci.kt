package ru.barabo.observer.config.barabo.plastic.release.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.release.cycle.StateRelease
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CheckWaitOci: Periodical {

    private val logger = LoggerFactory.getLogger(CheckWaitOci::class.java)

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override val count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(7 , 0), LocalTime.of(10 , 0))

    override fun name(): String = "Проверка зависших OCI-записей"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override fun execute(elem: Elem): State {

        val settingSession = AfinaQuery.uniqueSession()
        try {
            processWaitOci(settingSession)

        } catch (e: Exception) {
            logger.error("processFile", e)

            AfinaQuery.rollbackFree(settingSession)

            throw SessionException(e.message?:"")
        }

        AfinaQuery.commitFree(settingSession)

        return State.OK
    }

    private fun processWaitOci(settingSession: SessionSetting) {
        val waitList = AfinaQuery.select(query = SELECT_WAIT_OCI, sessionSetting = settingSession)

        waitList.forEach {

            val idContent = waitList[0] as Number

            val line = waitList[1] as String

            val fileName = waitList[2] as String

            GetOciData.processOciLine(idContent, line, fileName, settingSession)

            AfinaQuery.execute(UPDATE_WAIT_STATE_END, arrayOf(idContent))
        }
    }

    private const val UPDATE_WAIT_STATE_END = "update od.PTKB_OIA_WAIT_BY_OCI o set state = 1 where CONTENT_ID = ?"

    private val SELECT_WAIT_OCI = "select o.CONTENT_ID, o.line_row, o.file_name from od.PTKB_OIA_WAIT_BY_OCI o, " +
            "od.ptkb_plast_pack_content pc " +
            "where o.CONTENT_ID = pc.id and pc.state = ${StateRelease.RESPONSE_OK_ALL.dbValue}  and o.state = 0"
}
