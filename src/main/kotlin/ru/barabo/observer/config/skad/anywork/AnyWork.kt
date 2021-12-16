package ru.barabo.observer.config.skad.anywork

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.cbr.ptkpsd.task.CheckerDoubleTurnPeriod
import ru.barabo.observer.config.cbr.ptkpsd.task.CheckerRedSaldoNow
import ru.barabo.observer.config.cbr.ptkpsd.task.ClearPrimFromArchiveDay

object AnyWork : AbstractConfig() {

    override fun timeOut(): Long = 20_000

    override fun name() :String = "Различные действия"

    override fun configRun() {

        CheckerRedSaldoNow.findAll()
        CheckerDoubleTurnPeriod.findAll()

        NbkiAllReportsSend.findAll()
        //UnlockUsersMonday.findAll()
        CecReportProcess.findAll()

        CheckOpenArchiveDay.findAll()
        ExecOpenArchiveDay.findAll()
        RestrictUsersArchiveDay.findAll()
        CheckActializationEntryDouble.findAll()

        ExecuteReglamentRun.findAll()
        ExecuteGroupRateLoan.findAll()
        CorrDepartmentGoHome.findAll()
        ClearPrimFromArchiveDay.findAll()

        MantisUserDisabled.findAll()

        executeTasks()
    }
}