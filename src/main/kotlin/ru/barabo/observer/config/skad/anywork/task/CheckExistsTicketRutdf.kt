package ru.barabo.observer.config.skad.anywork.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Date

object CheckExistsTicketRutdf : Periodical {

    override var lastPeriod: LocalDateTime? = null

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(9, 0), LocalTime.of(16, 0), Duration.ZERO
    )

    override fun name(): String = "Проверить RUTDF-квитки"

    override fun config(): ConfigTask = AnyWork

    override fun execute(elem: Elem): State {

        isExistsYesterdayFile()

        val absents = AfinaQuery.select(SELECT_ABSENT_TICKET2)

        if (absents.isEmpty()) return State.OK

        for (absent in absents) {
            val fileName = absent[0] as String

            val dateFile = absent[1] as Date

            sendMailAbsentTicket(fileName, dateFile)
        }

        return State.ERROR
    }

    private fun isExistsYesterdayFile() {

        val isExistsYesterday = (AfinaQuery.selectValue(SELECT_IS_EXISTS_YESTERDAY_FILE) as Number).toInt() != 0

        if(!isExistsYesterday) {
            sendMailAbsentYesterdayFile()
        }
    }

    private fun sendMailAbsentTicket(fileName: String, dateFile: Date) {
        BaraboSmtp.sendStubThrows(
            to = BaraboSmtp.AUTO,
            subject = "Нет квитка на отправленный файл RUTDF",
            body = "Нет квитка на отправленный $dateFile файл RUTDF $fileName"
        )
    }

    private fun sendMailAbsentYesterdayFile() {
        BaraboSmtp.sendStubThrows(
            to = BaraboSmtp.AUTO,
            subject = "вчера НЕ отправлялся файл в НБКИ",
            body = "В предыдущий рабочий день вообще не отправлялся файл в НБКИ. Это ненормально, проверьте все ли в порядке."
        )
    }
}

private const val SELECT_IS_EXISTS_YESTERDAY_FILE = "select od.PTKB_RUTDF.IS_EXISTS_YESTERDAY_FILE from dual"

private const val SELECT_ABSENT_TICKET2 = """
 select f.file_name, f.date_file
from od.ptkb_rutdf_file f
where f.date_file >= date '2023-04-01'
  and f.date_file < trunc(sysdate)
  and f.state = 0"""