package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

object ExecuteExchangeCb : SingleSelector {
    override val select: String = "select dt.classified from doctree dt where dt.doctype = 1000131174 " +
            "and dt.docstate = 1000000039 and trunc(dt.validfromdate) = trunc(sysdate) and rownum = 1"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(8, 0), LocalTime.of(18, 0), Duration.ZERO)

    override fun name(): String = "Утверждение курса ЦБ"

    override fun config(): ConfigTask = PlasticOutSide

    override fun execute(elem: Elem): State {

        if((!AfinaQuery.isWorkDayNow()) ||
            LocalTime.now().hour >= 17 ) {

            return executeCbrRate(elem.idElem!!)
        }

        if(LocalTime.now().hour < 11) {
            return nextTimeCheckState(elem, 11)
        }

        val isExistsArchiveOpenDay = AfinaQuery.selectValue(SELECT_ARCHIVE_OPEN) != null

        if(isExistsArchiveOpenDay) {
            return nextTimeCheckState(elem)
        }

        val isCloseArchiveDayOldTime = (AfinaQuery.selectValue(SELECT_ARCHIVE_CLOSE_OLD_TIME) as Number).toInt()

        if(isCloseArchiveDayOldTime == 0) {
            return nextTimeCheckState(elem)
        }

        return executeCbrRate(elem.idElem!!)
    }

    private fun executeCbrRate(afinaDocument: Number): State {

        AfinaQuery.execute(EXEC_EXCHANGE_CBR, params = arrayOf(afinaDocument))

        return State.OK
    }

    private fun nextTimeCheckState(elem: Elem, setNexHour: Int = 0): State {

        if(setNexHour == 0) {
            elem.executed = LocalDateTime.now().plusMinutes(3)
        } else {
            elem.executed = LocalDateTime.now().withHour(setNexHour).withMinute(0).withSecond(0)
        }

        return State.NONE
    }

    private const val EXEC_EXCHANGE_CBR = "{ call od.PTKB_PRECEPT.executeAfterFixedCbrExchange( ? ) }"

    private const val SELECT_ARCHIVE_OPEN = "select od.PTKB_PRECEPT.findArchiveOpenDay from dual"

    private const val SELECT_ARCHIVE_CLOSE_OLD_TIME = "select od.PTKB_PRECEPT.isCloseArchiveDayLastTime from dual"
}