package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

object RecreateAfterError311p : SingleSelector {

    override fun name(): String = "311-П Выгрузить Счет после ошибки"

    override fun config(): ConfigTask = CryptoConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(11, 0),
            LocalTime.of(15, 59), null)

    override val select: String = """select r.id, a.code || ' ' || decode(r.type_send, 0, 'О ' || to_char(a.opened, 'dd.mm.yy'), 'З ' || to_char(a.closed,'dd.mm.yy') )
from od.ptkb_361p_register r
   , od.account a
where r.id_register is null and r.state = 8
 and r.idaccount = a.doc"""

    override fun execute(elem: Elem): State {
        AfinaQuery.execute(EXEC_RECREATE_MESSAGE, arrayOf(elem.idElem))

        return State.OK
    }

    private const val EXEC_RECREATE_MESSAGE = "{ call od.PTKB_FNS_EXPORT_XML.reCreateMessageAfterError(?) }"
}