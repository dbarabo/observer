package ru.barabo.observer.config.cbr.other.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
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

object CorrDepartmentGoHome : Periodical {
    val logger = LoggerFactory.getLogger(CorrDepartmentGoHome::class.java)!!

    override val unit = ChronoUnit.DAYS

    override var count = 1L

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(19, 15), LocalTime.of(21, 0), Duration.ZERO)

    override fun name(): String = "Коррсчет свалил домой"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        return if(isGoHomeCorrDepartment() ) sendMailGoHome() else waitToNextTime(elem, MINUTE_WAIT_NEXT_CHECK)
    }

    private fun waitToNextTime(elem: Elem, nextTime: Long): State {
        elem.executed = LocalDateTime.now().plusMinutes(nextTime)

        return if(elem.created.toLocalDate() != elem.executed!!.toLocalDate()) State.OK else State.NONE
    }

    private fun isGoHomeCorrDepartment(): Boolean {
        val isExists = AfinaQuery.selectValue(IS_EXISTS_CORR_DEPARTMENT) as Number

        return isExists.toInt() == 0
    }

    private fun sendMailGoHome(): State {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_GO_HOME, body = SUBJECT_GO_HOME)

        return State.OK
    }

    private const val SUBJECT_GO_HOME = "Пора домой - коррсчета уже нет!!!"

    private const val IS_EXISTS_CORR_DEPARTMENT = "SELECT od.PTKB_PRECEPT.isExistsCorrespondDepartment from dual"

    private const val MINUTE_WAIT_NEXT_CHECK: Long = 1
}