package ru.barabo.observer.config.cbr.ptkpsd.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

object ClearPrimFromArchiveDay : SingleSelector {

    private val logger = LoggerFactory.getLogger(ClearPrimFromArchiveDay::class.java)

    override val select: String = """
select co.doc, to_char(co.arcdate, 'dd.mm.yyyy')
from od.changeoperdate co,
  od.doctree dt
where co.doctype = 1007403095
  and dt.classified = co.doc
  and dt.sysfilial = 1
  and co.sysfilial = 1
  and dt.docstate = 1000000039
  and rownum = 1
    """

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(4, 0),
            workTimeTo = LocalTime.of(23, 55), executeWait = null)

    override fun name(): String = "Пересчитать показатели с арх. даты в РУБ"

    override fun config(): ConfigTask = PtkPsd

    override fun execute(elem: Elem): State {
        AfinaQuery.execute(query = EXEC_CLEAR_PRIM, params = arrayOf(elem.idElem))

        AfinaQuery.execute(query = CALC_RUR_PRIM, params = arrayOf(elem.idElem))

        return State.OK
    }

    private const val EXEC_CLEAR_PRIM = "{ call od.PTKB_PRECEPT.clearPrimAfter(?) }"

    private const val CALC_RUR_PRIM = "{ call od.PTKB_PRECEPT.calcPrimRur(?) }"


}