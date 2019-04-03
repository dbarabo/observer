package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object CheckTicketFns311p : SingleSelector {

    override val select: String ="""
select r.id
from od.ptkb_361p_register r
where r.id_register is null and r.state != 9
 and r.created > trunc(sysdate, 'YYYY')
 and sysdate - r.created - od.countHoliday(r.created, sysdate) > 2
 and rownum = 1"""

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(12, 0),
            workTimeTo = LocalTime.of(14, 0), executeWait = Duration.ofSeconds(1))

    override fun name(): String = "311-П Нет квитков из ФНС"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem) : State {

        val text = AfinaQuery.select(SELECT_FILES).joinToString("\n") { rowDataToString(it)  }

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_311P_ERROR, body = errorMessage(text) )

        return State.OK
    }

    private const val SELECT_FILES = """
select r.file_name, to_char(r.created, 'dd.mm.yy hh24:mi')
from od.ptkb_361p_register r
where r.id_register is null and r.state != 9
 and r.created > trunc(sysdate, 'YYYY')
 and sysdate - r.created - od.countHoliday(r.created, sysdate) > 2"""

    private const val SUBJECT_311P_ERROR = "311-П Ошибка в квитках ФНС"

    private fun errorMessage(files: String) = "На отправленные файлы до сих пор нет квитанций от ИФНС \n$files"

    private fun rowDataToString(row: Array<Any?>) = "Файл: ${row[0]}\t создан: ${row[1]}"
}