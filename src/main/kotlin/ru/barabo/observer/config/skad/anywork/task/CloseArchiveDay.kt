package ru.barabo.observer.config.skad.anywork.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.task.findBySelect
import ru.barabo.observer.config.cbr.ptkpsd.task.isCloseArchiveDay
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

private val logger = LoggerFactory.getLogger(CloseArchiveDay::class.java)

object CloseArchiveDay : Executor, ActionTask {

    override fun name(): String = "ЗАКРЫТЬ Архив. день"

    override fun config(): ConfigTask = AnyWork

    override fun actionTask(): ActionTask = this

    override fun findAbstract(): Executor? = findBySelect(null)

    override val accessibleData: AccessibleData = AccessibleData(
        workTimeFrom = LocalTime.of(9, 0),
        workTimeTo = LocalTime.of(21, 0),
        executeWait = null
    )

    override fun execute(elem: Elem): State {

        if (isCloseArchiveDay(elem.idElem)) {
            return State.OK
        }

        val anotherSessionSetting = AfinaQuery.uniqueSession(true)

        try {
            val (idArchive, isNeedCreateActual) =  AfinaQuery.execute(EXEC_CLOSE_ARCHIVE_DAY, null,
                anotherSessionSetting, intArrayOf(OracleTypes.NUMBER, OracleTypes.NUMBER) )!!


            logger.error("idArchive=$idArchive isNeedCreateActual =$isNeedCreateActual")

        } catch (e: Exception) {
            logger.error("execute", e)

            AfinaQuery.rollbackFree(anotherSessionSetting)

            throw Exception(e)
        }

        AfinaQuery.commitFree(anotherSessionSetting)

        return State.OK
    }
}

private const val EXEC_CLOSE_ARCHIVE_DAY = "{ call od.PTKB_PRECEPT.closeArchiveDay(?, ?) }"