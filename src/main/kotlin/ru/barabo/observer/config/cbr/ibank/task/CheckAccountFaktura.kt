package ru.barabo.observer.config.cbr.ibank.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ibank.IBank
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CheckAccountFaktura : SinglePerpetual {
    override val unit: ChronoUnit = ChronoUnit.HOURS

    override val countTimes: Long = 1

    override fun config(): ConfigTask = IBank

    override fun name(): String = "Проверка счетов в фактуре"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(7, 0), workTimeTo = LocalTime.of(22, 0))

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_CHECK_ACCOUNTS_IN_FAKTURA)

        return State.NONE
    }

    private const val EXEC_CHECK_ACCOUNTS_IN_FAKTURA = "{ call od.PTKB_IBANK.checkConnectAccountToFaktura }"
}