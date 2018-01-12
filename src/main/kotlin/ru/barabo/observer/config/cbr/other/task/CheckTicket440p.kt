package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object CheckTicket440p : SingleSelector {

    override val select: String =
            "select id from od.ptkb_440p_response where state != 99 and od.getWorkDayBack(sysdate, 3) > sent and rownum = 1"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(11, 0),
            workTimeTo = LocalTime.of(14, 0), executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Нет квитков из ФНС"

    override fun config(): ConfigTask = PtkPsd

    private val SELECT_FILES = "select r.file_name, to_char(r.sent, 'dd.mm.yy hh24:mi:ss') " +
            "from od.ptkb_440p_response r where r.state != 99 and od.getWorkDayBack(sysdate, 3) > r.sent"

    override fun execute(elem: Elem) :State {

        val text = AfinaQuery.select(SELECT_FILES).joinToString("\n") { rowDataToString(it)  }

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_440P_ERROR, body = errorMessage(text) )

        return State.OK
    }

    private val SUBJECT_440P_ERROR = "440-П Ошибка"

    private fun errorMessage(files :String) = "На отправленные файлы до сих пор нет квитанций от ИФНС \n$files"

    private fun rowDataToString(row :Array<Any?>) = "Файл: ${row[0]}\t создан: ${row[1]}"
}