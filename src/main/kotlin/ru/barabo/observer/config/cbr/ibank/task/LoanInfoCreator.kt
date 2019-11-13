package ru.barabo.observer.config.cbr.ibank.task

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ibank.IBank
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.lang.Exception
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object LoanInfoCreator : Periodical {
    override fun config(): ConfigTask = IBank

    override fun name(): String = "Создать инфо по кредитам LoanInfo"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(12, 0),
            workTimeTo = LocalTime.of(19, 0) )

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override fun execute(elem: Elem): State {

        try {
            AfinaQuery.execute(EXEC_CREATE_LOAN_INFO)
        } catch (e: SessionException) {

            BaraboSmtp.sendStubThrows(to = BaraboSmtp.OPER, subject = name(), body = e.message)
            return State.ARCHIVE
        }

        return State.OK
    }

    private const val EXEC_CREATE_LOAN_INFO = "{ call od.dpc_ptkb_unloadloaninfo }"
}