package ru.barabo.observer.config.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime
import java.time.LocalTime


interface Executor {

    fun executedTime(timeCreated: LocalDateTime = LocalDateTime.now()): LocalDateTime? =
            accessibleData.executeWait?.let { timeCreated.plusSeconds(it.seconds) }

    val accessibleData: AccessibleData

    fun myClassName() =  Executor::class.qualifiedName ?: "Executor::class"

    fun actionTask(): ActionTask

    fun findAbstract(): Executor?

    fun isAccess(): Boolean = isWorkTime() && isWeekAccess()

    fun findAll(): Executor? = sendFindAllError { if(isAccess()) findAbstract() else null }

    private fun isWorkTime(time: LocalTime = LocalTime.now()): Boolean =
            if(accessibleData.workTimeFrom < accessibleData.workTimeTo) {
                (accessibleData.workTimeFrom <= time && time <= accessibleData.workTimeTo)
            } else {
                (accessibleData.workTimeFrom <= time || time <= accessibleData.workTimeTo)
            }

    fun isWorkTimeByTime(time: LocalTime): Boolean = isWorkTime(time)


    private fun isWeekAccess(): Boolean = accessibleData.workWeek == WeekAccess.ALL_DAYS || AfinaQuery.isWorkDayNow()

    fun executeElem(elem: Elem) {

        if(elem.state == State.OK || elem.state == State.PROCESS || elem.state == State.ARCHIVE) return

        synchronized(elem.state) { elem.state = State.PROCESS}

        try {
            val state = elem.task!!.execute(elem)

            synchronized(elem.state) { elem.state = state}

        } catch (e: Exception) {

            LoggerFactory.getLogger(Executor::class.java).error("execute", e)

            synchronized(elem.state) { elem.state = State.ERROR }
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

        val elemErrorFull = elem.copy()

        elem.error = elem.error?.let{ if(it.length <= 500) it else it.substring(0, 500) }

        StoreSimple.save(elem)

        checkSendMailError(elemErrorFull)
    }

    private fun sendFindAllError(process: ()->Executor?): Executor? =
        try {
            process()
        } catch (e: Exception) {
            sendFindAllError(e)

            null
        }

    private fun sendFindAllError(e: Exception) {
        BaraboSmtp.sendStubThrows(to = BaraboSmtp.YA, subject = "Ошибка поиска ${myClassName()}", body = getStackTrace(e))
    }

    private fun getStackTrace(throwable: Throwable): String {
        val sw = StringWriter()

        val pw = PrintWriter(sw, true)

        throwable.printStackTrace(pw)

        return sw.buffer.toString()
    }

    fun checkSendMailError(elem: Elem) {
        if(elem.state != State.ERROR || elem.task?.isSendError()?.not() == true) return

        BaraboSmtp.errorSend(elem)
    }

    private fun executeAll(): Executor? {

        val items = StoreSimple.getItems(State.NONE) { it == actionTask() }

        items.forEach { executeElem(it) }

        //BaraboSmtp.sendSuspendElem()

        return if(items.isEmpty()) null else this
    }
}