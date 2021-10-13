package ru.barabo.observer.config.barabo.plastic.turn.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi.saveFile
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

object OutInnByMerchant : SingleSelector {

    override fun name(): String = "Выгрузка Zil-файлов (ИНН)"

    override fun config(): ConfigTask = PlasticTurnConfig

    override val accessibleData: AccessibleData = AccessibleData(
        workTimeFrom = LocalTime.of(10, 0), workTimeTo = LocalTime.of(16, 0))

    override val select: String = """
select min(p.classified), min(p.terminalid)
from od.ptkb_poses p
left join od.ptkb_zil_record zl on zl.merchant_id = p.merchant_id
where coalesce(p.validto, sysdate+1) > sysdate
  and zl.id is null
  and p.merchant_id is not null
having min(p.classified) is not null   
"""

    override fun execute(elem: Elem): State {

        val idZil = AfinaQuery.execute(EXEC_FILL_ZIL, outParamTypes = intArrayOf(OracleTypes.NUMBER))?.get(0) as Number

        val zilInfo = AfinaQuery.selectCursor(SELECT_ZIL, arrayOf(idZil))

        if( zilInfo.isEmpty() ) return State.OK

        val zilFileName = AfinaQuery.selectValue(SELECT_ZIL_FILENAME, arrayOf(idZil) ) as String

        val zilInfoText = createTextInfo(zilInfo)

        saveFile( zilFileName, zilInfoText )

        AfinaQuery.execute(EXEC_STATE_ZIL, arrayOf(idZil))

        return State.OK
    }

    private fun createTextInfo(zilInfo: List<Array<Any?>>): String =
        zilInfo.joinToString("\n") { it[0] as String }
}

private const val EXEC_FILL_ZIL = "{ call od.PTKB_PLASTIC_TURN.fillZil( ? ) }"

private const val SELECT_ZIL = "{ ? = call od.PTKB_PLASTIC_TURN.getZilData( ? ) }"

private const val SELECT_ZIL_FILENAME = "select od.PTKB_PLASTIC_TURN.getZilFileName( ? ) from dual"

private const val EXEC_STATE_ZIL = "{ call od.PTKB_PLASTIC_TURN.execStateZil( ? ) }"