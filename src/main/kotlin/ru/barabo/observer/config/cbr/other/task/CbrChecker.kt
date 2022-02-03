package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mail.smtp.CbrSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CbrChecker : SinglePerpetual {

    override fun name(): String = "Проверка связи с ЦБ"

    override fun config(): ConfigTask = OtherCbr

    override val accessibleData: AccessibleData =
        AccessibleData(workTimeFrom =  LocalTime.of(8, 0), workTimeTo = LocalTime.of(21, 30))

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 15

    override fun execute(elem: Elem): State {

        CbrSmtp.checkCbr()

        return super.execute(elem)
    }
}