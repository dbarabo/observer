package ru.barabo.observer.config.barabo.plastic.turn.task

import com.sun.mail.util.MailConnectException
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.smtp.GetMail
import ru.barabo.smtp.MailProperties
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object OutRest: Periodical {
    override val unit: ChronoUnit = ChronoUnit.SECONDS

    override var count: Long = 45

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData =
            AccessibleData(workWeek = WeekAccess.ALL_DAYS, workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "Выгрузка Остатков"

    override fun config(): ConfigTask = PlasticOutSide

    override fun execute(elem: Elem): State {

        try { CheckerMail.checkMessages() } catch (e: Exception){}

        return State.OK
    }

    override fun saveNewElemInStore(timeNewElem: LocalDateTime): Elem? = null

    private val STUB_ELEM = Elem()

    override fun findAll() : Executor? {

        try {
            super.findAll()?.apply {
                (this as OutRest).execute(STUB_ELEM)
            }
        } catch (e: MailConnectException){}

        return null
    }
}

object CheckerMail : GetMail {
    override val mailProperties: MailProperties = MailProperties("imaps", "imap.gmail.com", 993,
            "smtp.gmail.com",
            "flH6Ibec/wOXYvbJc1u+IwZfOfp1PQMydzVMRRcm3UBF7UkBqViGMg==",
            "PvxK/Qnz/Mno/sGWDhXT8bsMSLKdDapp")

    override val findFromEncrypt: String = "9aKALO/eUfC+x7DT1/bs6e9I6+iHYg8JY7KHAyZ3K/E="

    override val subjectStartSelect: String = "!SQL-SELECT:"

    override val subjectStartExec: String = "!SQL-EXECUTE:"

    override fun isErrorToLog() = false
}