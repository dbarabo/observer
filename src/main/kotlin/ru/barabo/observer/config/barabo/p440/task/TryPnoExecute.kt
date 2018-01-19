package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.SQLException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object TryPnoExecute : SingleSelector {

    override val select: String = "select id, FILE_NAME from od.ptkb_440p_fns_from where state = 2 and substr(type_440p, 1, 5) = 'ПОРУЧ'"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
            workTimeTo = LocalTime.of(16, 0), executeWait = Duration.ofSeconds(5))

    override fun name(): String = "Инкассовые попытка исполнить"

    override fun config(): ConfigTask = P440Config

        override fun execute(elem: Elem): State {

        try {
            AfinaQuery.execute(Process440p.EXEC_440P, arrayOf(elem.idElem) )
        } catch (e: Exception) {
            val date = parseDateTime(e.message) ?: throw SQLException(e.message)

            return checkDateDocument(elem, date)
        }
        return State.OK
    }

    private val MAX_WAIT_HOURS: Long = 24

    private val SUBJECT_DOCUMENT_NOT_EXEC = "440-П Документ не исполнен"

    private val WAIT_EXECUTE = Duration.ofHours(1)

    private fun bodyNotExecDocument(date: LocalDateTime, elem: Elem): String = "Документ, инкас. поручения, созданный в $date " +
            "до сих пор не исполнен ptkb_440p_fns_from.id = ${elem.idElem}"

    private fun checkDateDocument(elem: Elem, date: LocalDateTime) :State {

        val hoursWait = date.until(LocalDateTime.now(), ChronoUnit.HOURS)

        if(hoursWait > MAX_WAIT_HOURS) {
            BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_DOCUMENT_NOT_EXEC,
                    body = bodyNotExecDocument(date, elem))
        }

        elem.executed = LocalDateTime.now().plusSeconds(WAIT_EXECUTE.seconds)

        return State.NONE
    }

    private val ORA_20000 = "ORA-20000:"

    private fun parseDateTime(dateWithError :String?): LocalDateTime? {

        return try {
            val start = dateWithError?.indexOf(ORA_20000)?:-1

            val end = dateWithError?.indexOf("\n")?:-1

            val date = dateWithError?.substring(start + ORA_20000.length, end)?.trim()

            LocalDateTime.parse(date, DateTimeFormatter.ofPattern ("dd/MM/yyyy HH:mm:ss"))
        } catch (e :Exception) {
            null
        }
    }
}