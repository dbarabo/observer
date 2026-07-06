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
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

private val logger = LoggerFactory.getLogger(CloseArchiveDay::class.java)

object CloseArchiveDay : Executor, ActionTask {

    override fun name(): String = "ЗАКРЫТЬ Архив. день"

    override fun config(): ConfigTask = AnyWork

    override fun actionTask(): ActionTask = this

    override val accessibleData: AccessibleData = AccessibleData(
        workTimeFrom = LocalTime.of(9, 0),
        workTimeTo = LocalTime.of(21, 0),
        executeWait = Duration.ofHours(9)
    )

    override fun findAbstract(): Executor? {

        val closeTime = if(LocalDateTime.now().hour < 17)
                            LocalDateTime.now().withHour(18).withMinute(0)
                        else
                            LocalDateTime.now().plusDays(1).withHour(18).withMinute(0)

        return findBySelect(closeTime)
    }


    override fun execute(elem: Elem): State {

        if (isCloseArchiveDay(elem.idElem)) {
            return State.OK
        }

        if(LocalTime.now().hour >= 21) {
            elem.executed = LocalDateTime.now().plus((10 + (24 - LocalTime.now().hour)).toLong(), ChronoUnit.HOURS)
        }

        if(AfinaQuery.isHoliday(LocalDate.now()) ) {
            elem.executed = LocalDateTime.now().plus(1, ChronoUnit.DAYS)
            return State.NONE
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