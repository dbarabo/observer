package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime

object CheckTicketArchive311p : SingleSelector {

    override val select: String = """
select a.id, a.file_name
from od.ptkb_361p_archive a
where (sysdate > a.sent + 1/24
      or
       sysdate >= trunc(sysdate) + (15.5/24)
      )
      and (  a.state != 9
          or a.return_code != 'ПРИНЯТ'
      )
    """

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(11, 0),
            workTimeTo = LocalTime.of(17, 0))

    override fun name(): String = "Нет квитков на Архив 311-П"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        val data = AfinaQuery.select(SELECT_ARCHIVE_INFO, arrayOf(elem.idElem)).first()

        val state = (data[0] as Number).toInt()

        val resultCode = data[1] as String

        if((state == 9) && (resultCode == "ПРИНЯТ")) return State.OK

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_311P_ERROR,
                body = errorMessage(elem.name, data[2] as String, resultCode) )

        return if(state == 9) {
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