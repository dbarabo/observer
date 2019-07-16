package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ibank.IBank
import ru.barabo.observer.config.cbr.other.task.form101.BalanceChecker101f
import ru.barabo.observer.config.cbr.other.task.form101.CheckerAbsentBalance
import ru.barabo.observer.config.cbr.other.task.form101.CheckerDoubleTurn
import ru.barabo.observer.config.cbr.other.task.form101.CheckerRedSaldo
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CheckerAllBalance : Periodical {

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData =
            AccessibleData(workTimeFrom = LocalTime.of(23, 0), workTimeTo = LocalTime.of(23, 59, 59))

    override fun name(): String = "Проверка баланса за 20 дат"

    override fun config(): ConfigTask = IBank

    override fun execute(elem: Elem): State {

        val dates = AfinaQuery.selectCursor(CURSOR_20_LAST_DATES)

        val checkUpdateDate = if(dates.size >= 10) dates[9] else emptyArray()

        for (datePair in dates) {

            val dateOn = datePair[0] as Timestamp

            BalanceChecker101f.check101form(datePair[1] as Number, dateOn)

            CheckerRedSaldo.isCheckSaldo(dateOn)

            if(datePair === checkUpdateDate) {
                IBank.checkWorkConfig()
            }
        }

        CheckerDoubleTurn.checkDouble()

        CheckerAbsentBalance.checkAbsent()

        return State.OK
    }

    private const val CURSOR_20_LAST_DATES = "{ ? = call od.PTKB_PRECEPT.getLast20Times101Forms }"
}