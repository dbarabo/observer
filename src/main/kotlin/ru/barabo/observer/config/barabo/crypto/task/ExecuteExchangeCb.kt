package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.config.test.TestConfig
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

object ExecuteExchangeCb : SingleSelector {
    override val select: String = "select * from dual"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.MAX, workTimeTo = LocalTime.MAX, executeWait = null)

    override fun name(): String = "stub"

    override fun config(): ConfigTask = TestConfig

    override fun execute(elem: Elem): State = State.ERROR
}