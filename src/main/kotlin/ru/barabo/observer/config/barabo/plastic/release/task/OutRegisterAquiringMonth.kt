package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.config.barabo.plastic.release.registers.OutRegisterAquiringRange
import java.time.LocalDate

object OutRegisterAquiringMonth : OutRegisterAquiringRange() {
    override fun selectActionNow(): String =
            "select nullif(od.getWorkDayBack(trunc(sysdate), 2), od.getNextWorkDay(trunc(sysdate, 'MM')-1) ) from dual"

    override fun nameRegister(): String = "${ LocalDate.now().monthValue}_month"

    override fun subjectRegister(): String = "Реестр транзакций за месяц"

    override fun selectCursorTerminals(): String = "{? = call od.PTKB_PLASTIC_TURN.selectMonthAquiringTerminals }"

    override fun selectCursorTransactRegisters(): String = "{? = call od.PTKB_PLASTIC_TURN.getRegistersByMonth(?, ?) }"
}
