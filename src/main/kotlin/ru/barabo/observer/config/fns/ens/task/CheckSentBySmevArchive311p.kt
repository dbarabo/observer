package ru.barabo.observer.config.fns.ens.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object CheckSentBySmevArchive311p : SingleSelector {
    override fun name(): String = "311-П Проставить отправку в СМЭВе"

    override fun config(): ConfigTask = EnsConfig

    override val select: String = """
        select a.id, upper(a.file_name) || '.ZIP'
from od.ptkb_361p_archive a
where a.state = 1
  and upper(a.file_name) || '.ZIP' in 
   (select upper(filename)
      from od.ptkb_fns_311_p
      union 
      select upper(filename)
      from od.ptkb_sfr_361_p) 
    """

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(9, 0),
        workTimeTo = LocalTime.of(19, 0), executeWait = Duration.ofSeconds(15))

    override fun execute(elem: Elem): State {

        val select = if(elem.name.indexOf('N') == 0) SELECT_FNS else SELECT_SFR

        val (code, date)  = AfinaQuery.select(select, arrayOf(elem.name))[0]

        AfinaQuery.execute(SET_STATE_SEND_SMEV, arrayOf(elem.idElem, code, date) )

        return State.OK
    }
}

private const val SET_STATE_SEND_SMEV = "{ call od.PTKB_440P.saveStateSendSmev(?, ?, ?) }"

private const val SELECT_FNS = "select nvl(p.result_code, -1), p.datetime from od.ptkb_fns_311_p p where upper(p.filename) = ?"

private const val SELECT_SFR = "select nvl(p.result_code, -1), p.datetime from od.ptkb_sfr_361_p p where upper(p.filename) = ?"