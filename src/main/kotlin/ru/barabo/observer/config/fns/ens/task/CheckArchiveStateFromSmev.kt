package ru.barabo.observer.config.fns.ens.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CheckArchiveStateFromSmev : SinglePerpetual {

    override fun name(): String = "Квиток на Архив из СМЭВ"

    override fun config(): ConfigTask = EnsConfig

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 15

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
        workTimeFrom = LocalTime.of(6, 0), workTimeTo = LocalTime.of(23, 0) )

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(CHECK_STATE_ARCHIVE_IN_SMEV)

        return super.execute(elem)
    }
}

private const val CHECK_STATE_ARCHIVE_IN_SMEV = "{ call od.PTKB_440P.checkStateArchiveInSmev }"