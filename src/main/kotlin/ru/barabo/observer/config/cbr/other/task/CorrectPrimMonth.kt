package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp
import java.time.LocalTime

object CorrectPrimMonth : SingleSelector {

    override val select: String = """
        select id, to_char(date_report, 'dd.mm.yyyy') from od.ptkb_ptkpsd_101form where state = 0
              and upper(TYPE_REPORT) = upper('месячная') and date_report > date '2021-02-02' order by date_report"""

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
        workTimeFrom = LocalTime.of(8, 0), workTimeTo = LocalTime.of(22, 0) )

    override fun name(): String = "Правка показателей Месячная."

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        val reportDate = AfinaQuery.selectValue(CorrectPrim.SELECT_DATE, arrayOf(elem.idElem))

        AfinaQuery.execute(EXEC_MONTH_CORRECT_PRIM, arrayOf(reportDate))

        CorrectPrim.sendReportCorrect(elem.idElem, reportDate as Timestamp)

        return State.OK
    }
}

private const val EXEC_MONTH_CORRECT_PRIM = "call od.PTKB_PRECEPT.correctPrimBalanceMonth(?)"