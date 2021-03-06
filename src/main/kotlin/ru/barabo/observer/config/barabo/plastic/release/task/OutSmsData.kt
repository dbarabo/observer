package ru.barabo.observer.config.barabo.plastic.release.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.release.cycle.StateRelease
import ru.barabo.observer.config.barabo.plastic.release.cycle.TypePacket
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi.hCardOutToday
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.sql.Clob
import java.time.LocalTime

object OutSmsData: SingleSelector {
    override val select: String = """
select id, name from od.ptkb_plastic_pack
where state = ${StateRelease.OCI_ALL.dbValue}
  and type_packet not in ( ${TypePacket.BTRT15_SUSPEND.dbValue} , ${TypePacket.BTRT15_ACTIVE.dbValue} )
"""

    override fun name(): String = "SMS - on/off"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(6, 0))

    override fun execute(elem: Elem): State {

        val iiaFile = iiaFile()

        val data = AfinaQuery.execute(query = CREATE_FILE_SMS, params = arrayOf(elem.idElem, iiaFile),
                outParamTypes = intArrayOf(OracleTypes.CLOB))

        val text = data?.get(0)?.let { (it as Clob).clob2string() } ?: return State.OK

        val file = File("${hCardOutToday()}/$iiaFile")

        file.writeText(text, Charset.forName("CP1251"))

        SendToJzdo.executeFile(file)

        return State.OK
    }

    private const val CREATE_FILE_SMS = "{ call od.PTKB_PLASTIC_AUTO.createSmsFileData(?, ?, ?) }"

}

private const val SELECT_IIA_FILE = "select od.PTKB_PLASTIC_AUTO.getFileNameIIA from dual"

fun iiaFile(): String =  AfinaQuery.selectValue(SELECT_IIA_FILE) as String