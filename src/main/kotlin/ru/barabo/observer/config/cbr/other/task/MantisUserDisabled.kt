package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.mantis.MantisQuery
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object MantisUserDisabled : Periodical {

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1L

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(20, 0), workTimeTo = LocalTime.of(23, 0))

    override fun name(): String = "Залочить Уволенных в Мантисе"

    override fun config(): ConfigTask = AnyWork // OtherCbr

    override fun execute(elem: Elem): State {

        val mails = AfinaQuery.selectCursor(SELECT_EMAIL_LAID_OFF).joinToString {  "'${it[0]}'" }

        if(mails.isEmpty()) return State.OK

        MantisQuery.execute(deleteMantisEmails(mails) )

        MantisQuery.execute(updateMantisEnanabled(mails) )

        AfinaQuery.execute(updateCheckUserDisabled(mails))

        return State.OK
    }

    private const val SELECT_EMAIL_LAID_OFF = "{ ? = call od.PTKB_PLASTIC_REPORT.getMailLaidOffEmployees }"

    private fun updateMantisEnanabled(mails: String) =
            "update mantis_user_table set enabled = 0, email = '' where email in ($mails)"

    private fun deleteMantisEmails(mails: String) =
        "delete from mantis_email_table where email in ($mails)"

    private fun updateCheckUserDisabled(mails: String) = """
update od.users u
set u.lastchange = to_date('01/01/2019', 'dd/mm/yyyy')
where trim(od.GetQuestCodeValue('Communication', 'WorkMail', u.client)) in ($mails)
    """
}