package ru.barabo.observer.config.fns.cbr.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.cbr.CbrConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object ProcessCbrRequest : SingleSelector {

    override fun name(): String = "Обработка запроса ЦБ"

    override fun config(): ConfigTask = CbrConfig

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
        workTimeTo = LocalTime.of(20, 0), executeWait = Duration.ofSeconds(5))

    override val select: String =  "select id, FILENAME from od.PTKB_CBR_EXTRACT_MAIN_REQUEST where state = 0"

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_CBR_EXTRACT, arrayOf(elem.idElem) )

        return State.OK
    }
}

private const val EXEC_CBR_EXTRACT = "{ call od.PTKB_CBR_EXTRACT.process( ? ) }"