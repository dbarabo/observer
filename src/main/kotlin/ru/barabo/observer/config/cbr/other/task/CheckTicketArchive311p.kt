package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime

object CheckTicketArchive311p : SingleSelector {

    override fun name(): String = "Нет квитков на Архив 311-П"

    override fun config(): ConfigTask = EnsConfig

    override val select: String = """
select a.id, a.file_name
from od.ptkb_361p_archive a
where sysdate > a.sent + 30/(60*24)
  and ( (a.state in (0, 1) ) 
      or
      (a.state = 2 and coalesce(a.return_code, '-1') = '0')
      )
    """
    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(11, 0),
            workTimeTo = LocalTime.of(18, 0))

    override fun execute(elem: Elem): State {

        val data = AfinaQuery.select(SELECT_ARCHIVE_INFO, arrayOf(elem.idElem)).first()

        val state = (data[0] as Number).toInt()

        if(state == 9) return State.OK

        val resultCode = data[1] as String

        val resultStatus = when(resultCode) {
            "0" -> "Ошибка отправки архива!"
            "1" -> "Архив УСПЕШНО отправлен"
            else -> resultCode
        }

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_311P_ERROR,
                body = errorMessage(elem.name, data[2] as String, resultStatus) )

        return if(resultCode == "0") {
            State.ERROR
        } else {
            elem.executed = LocalDateTime.now().plusMinutes(20)

            State.NONE
        }
    }

    private const val SELECT_ARCHIVE_INFO = """
select a.state, coalesce(a.return_code, 'НЕ ПОЛУЧЕНА'), to_char(a.sent, 'dd.mm.yy hh24:mi:ss')
 from od.ptkb_361p_archive a where a.id = ?"""

    private const val SUBJECT_311P_ERROR = "311-П Ошибка квитка ЦБ на отправленный архив"

    private fun errorMessage(fileName: String, sentDate: String?, statusTicket: String?) =
            "На отправленный архив не получена квитанция или она с ошибкой \n" +
            "\tФайл архива: $fileName\n" +
            "\tОтправлен: $sentDate\n" +
            "\tСтатус квитанции: $statusTicket"
}