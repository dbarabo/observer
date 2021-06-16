package ru.barabo.observer.config.skad.plastic.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CheckSpaceServer : SinglePerpetual {

    private val logger = LoggerFactory.getLogger(CheckSpaceServer::class.java)

    override fun name(): String = "Проверка Места на Сервере"

    override fun config(): ConfigTask = PlasticOutSide

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 30

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(0, 20), workTimeTo = LocalTime.of(23, 50) )

    override fun execute(elem: Elem): State {

        for(path in paths) {
            val gb = (AfinaQuery.selectValue(SELECT_FREE_SPACE, arrayOf(path)) as? Number)?.toDouble()
                    ?: throw Exception("Невозможно вычислить остаток свободного места по адресу $path")

            logger.error("$path $gb")
            if(gb > 10) continue

            return sendInfoFreeSpace(path, gb)
        }

        return super.execute(elem)
    }

    private fun sendInfoFreeSpace(path: String, gbFree: Double): State {
        val smsText = "Alarm!!! Free space less ${formatDecimal2.format(gbFree)}GB in server disk path $path"

        for(phone in phones) {

            logger.error("phone=$phone")
            logger.error("smsText=$smsText")

            AfinaQuery.execute(EXEC_SMS_ADD, arrayOf(phone, smsText, 0))
        }

        val subject = "✖☢☠ Место на сервере заканчивается!!!"

        val body = "Осталось меньше ${formatDecimal2.format(gbFree)}GB на диске $path"

        logger.error("smtp=$body")
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, cc = BaraboSmtp.AUTO, subject = subject, body = body, charsetSubject = "UTF-8")

        return State.OK
    }

    private val formatDecimal2 = DecimalFormat("#.##", DecimalFormatSymbols().apply { decimalSeparator = '.' } )

    private val paths = arrayOf("/opt/oracle/oradata/", "/opt/oracle/backup/", "/usr/local/base_backup/")

    private val phones =arrayOf("79243351635", "79147909412", "79147032716", "79025245968")

    private const val SELECT_FREE_SPACE = "select od.USE_SPACE( ? )/1024 from dual"

    private const val EXEC_SMS_ADD = "insert into od.ptkb_sms (phone, text, reference)  values (?, ?, ?)"
}