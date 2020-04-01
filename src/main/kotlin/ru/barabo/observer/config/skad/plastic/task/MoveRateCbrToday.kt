package ru.barabo.observer.config.skad.plastic.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object MoveRateCbrToday : SingleSelector {

    override fun name(): String = "Курс ЦБР - перемещиние в сегодня"

    override fun config(): ConfigTask = PlasticOutSide

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(7, 0), LocalTime.of(8, 30), Duration.ofSeconds(1))

    override val select: String = """
select dt.classified 
from od.doctree dt 
where dt.doctype = 1000131174
  and dt.docstate = 1000000034 
  and trunc(dt.userdate) = trunc(sysdate)
and not exists (
  select *
  from od.doctree dt 
  where dt.doctype = 1000131174
    and dt.docstate in (1000000034, 1000000039, 1000000035)
    and trunc(dt.validfromdate) = trunc(sysdate)
)"""

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_UPDATE_RATE_CBR_TODAY, params = arrayOf(elem.idElem))

        return State.OK
    }

    private const val EXEC_UPDATE_RATE_CBR_TODAY = "{ call od.PTKB_PRECEPT.updateCbrRateToday(?) }"
}