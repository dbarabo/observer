package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

object CheckOpenArchiveDay : SingleSelector {

    override val select: String =
            "select co.doc, to_char(co.ArcDate, 'dd.mm.yyyy') from ChangeOperDate co where co.doc = od.PTKB_PRECEPT.findArchiveOpenDay and rownum = 1"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(19, 0),
            workTimeTo = LocalTime.of(22, 0))

    override fun name(): String = "Открыт Архивный день"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem) : State {

        val infoText = openArchiveDay(elem.name)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = infoText, body = infoText)

        return State.OK
    }

    private fun openArchiveDay(day: String) = "Открыт архивный день $day"
}