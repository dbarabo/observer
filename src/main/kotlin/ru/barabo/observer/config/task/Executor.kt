package ru.barabo.observer.config.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.time.LocalDateTime
import java.time.LocalTime

interface Executor {

    fun executedTime(timeCreated: LocalDateTime = LocalDateTime.now()): LocalDateTime? =
            accessibleData.executeWait?.let { timeCreated.plusSeconds(it.seconds) }

    val accessibleData: AccessibleData

    fun actionTask(): ActionTask

    fun findAbstract(): Executor?

    fun isAccess(): Boolean = isWorkTime() && isWeekAccess()

    fun findAll(): Executor? = if(isAccess()) findAbstract() else null

    private fun isWorkTime(time: LocalTime = LocalTime.now()): Boolean =
            if(accessibleData.workTimeFrom < accessibleData.workTimeTo) {
                (accessibleData.workTimeFrom <= time && time <= accessibleData.workTimeTo)
            } else {
                (accessibleData.workTimeFrom <= time || time <= accessibleData.workTimeTo)
            }

    fun isWorkTimeByTime(time: LocalTime): Boolean = isWorkTime(time)


    private fun isWeekAccess(): Boolean = accessibleData.workWeek == WeekAccess.ALL_DAYS || AfinaQuery.isWorkDayNow()

    fun executeElem(elem: Elem, isSuspend: Boolean = false) {
        synchronized(elem.state) {

            if(elem.state == State.OK || elem.state == State.PROCESS || elem.state == State.ARCHIVE) return

            elem.state = State.PROCESS

            try {
                elem.state = elem.task!!.execute(elem)

            } catch (e: Exception) {

                LoggerFactory.getLogger(Executor::class.java).error("execute", e)

                elem.state = State.ERROR
                elem.error = e.message
            }
            elem.executed = when (elem.state) {
                State.OK, State.ERROR -> {
                    LocalDateTime.now()
                }
                else -> {
                    elem.executed
                }
            }
        }

        val elemErrorFull = elem.copy()

        elem.error = elem.error?.let{ if(it.length <= 500) it else it.substring(0, 500) }

        StoreSimple.save(elem)

        checkSendMailError(elemErrorFull, isSuspend)
    }

    private fun checkSendMailError(elem: Elem, isSuspend: Boolean) {
        if(elem.state != State.ERROR || elem.task?.isSendError()?.not() == true) return

        if(!isSuspend) {
            BaraboSmtp.errorSend(elem)
        } else {
            BaraboSmtp.addSuspendElem(elem)
        }
    }

    private fun executeAll(): Executor? {

        val items = StoreSimple.getItems(State.NONE) { it == actionTask() }

        items.forEach { executeElem(it, isSuspend = false) }

        //BaraboSmtp.sendSuspendElem()

        return if(items.isEmpty()) null else this
    }
}