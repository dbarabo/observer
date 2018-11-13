package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.config.barabo.plastic.release.registers.OutRegisterAquiringRange
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

object OutRegisterAquiringWeek : OutRegisterAquiringRange() {

    override fun selectActionNow(): String =
            "select nullif(od.getNextWorkDay( od.getNextWorkDay(trunc(sysdate, 'IW')-1) ), trunc(sysdate) ) from dual"

    override fun nameRegister(): String {
        val weekFields = WeekFields.of(Locale.getDefault()).weekOfYear()

        val numberWeek = LocalDate.now().get(weekFields) - 1

        return "${numberWeek}_week"
    }

    override fun subjectRegister(): String = "Реестр транзакций за неделю"

    override fun selectCursorTerminals(): String = "{? = call od.PTKB_PLASTIC_TURN.selectWeekAquiringTerminals }"

    override fun selectCursorTransactRegisters(): String = "{? = call od.PTKB_PLASTIC_TURN.getRegistersByWeek(?, ?) }"
}