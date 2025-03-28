package ru.barabo.observer.config.skad.anywork

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.cbr.ptkpsd.task.CheckerDoubleTurnPeriod
import ru.barabo.observer.config.cbr.ptkpsd.task.CheckerRedSaldoNow
import ru.barabo.observer.config.cbr.ptkpsd.task.ClearPrimFromArchiveDay
import ru.barabo.observer.config.skad.anywork.task.*

object AnyWork : AbstractConfig() {

    override fun timeOut(): Long = 20_000

    override fun name() :String = "Различные действия"

    override fun configRun() {

        CloseArchiveDay.findAll()
        CheckerRedSaldoNow.findAll()
        CheckerDoubleTurnPeriod.findAll()

        UnlockUsersMonday.findAll()

        GutDfCreateFile.findAll()
        CheckExistsTicketRutdf.findAll()

        CecReportProcess.findAll()

        CheckOpenArchiveDay.findAll()
        ExecOpenArchiveDay.findAll()
        RestrictUsersArchiveDay.findAll()
        CheckActializationEntryDouble.findAll()

        ExecuteGroupRateLoan.findAll()
        ClearPrimFromArchiveDay.findAll()

        MantisUserDisabled.findAll()

        Extract407pByRfm.findAll()

        CbrKeyRateLoader.findAll()

        executeTasks()
    }
}