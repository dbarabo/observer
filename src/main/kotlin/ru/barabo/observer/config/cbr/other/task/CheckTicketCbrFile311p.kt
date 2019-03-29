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

object CheckTicketCbrFile311p : SingleSelector {

    override val select: String = """
select r.id, r.file_name
  from od.ptkb_361p_register r
 where r.id_register is null
   and sysdate >  r.created + 2/24
   and coalesce(r.ticket_archive, '!') != 'ПРИНЯТ'
"""

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(10, 0),
            workTimeTo = LocalTime.of(16, 30))

    override fun name(): String = "Нет квитков ЦБ файл 311-П"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        val data = AfinaQuery.select(SELECT_FILE_INFO, arrayOf(elem.idElem)).first()

        val ticketArchive = data[0] as String

        val  isRecreateFile = data[1] != null

        if(isRecreateFile || (ticketArchive == "ПРИНЯТ")) return State.OK

        val created = data[2] as? String

        val account = data[3] as? String

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_311P_ERROR,
                body = errorMessage(elem.name, created, ticketArchive, account) )

        return if(ticketArchive == "НЕ ПОЛУЧЕНА") {
            elem.executed = LocalDateTime.now().plusMinutes(20)

            State.NONE
        } else {
            State.ERROR
        }
    }

    private const val SELECT_FILE_INFO = """
select coalesce(r.ticket_archive, 'НЕ ПОЛУЧЕНА'), r.id_register,
to_char(r.created, 'dd.mm.yy hh24:mi:ss'), accountcode(r.idaccount)
  from od.ptkb_361p_register r
 where r.id = ?"""

    private const val SUBJECT_311P_ERROR = "311-П Ошибка квитка ЦБ на отправленный файл"

    private fun errorMessage(fileName: String, created: String?, statusTicket: String?, account: String?) =
            "На отправленный файл не получена квитанция из ЦБ или она с ошибкой \n" +
                    "\tФайл: $fileName\n" +
                    "\tСчет: $account\n" +
                    "\tСоздан: $created\n" +
                    "\tСтатус квитанции: $statusTicket"
}