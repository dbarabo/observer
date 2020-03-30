package ru.barabo.observer.config.task.template.periodic

import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

interface SinglePerpetual : Executor, ActionTask {
    override fun actionTask(): ActionTask = this

    override fun findAbstract(): Executor? = findLastItem()

    val unit: ChronoUnit

    val countTimes: Long

    override fun execute(elem: Elem): State {

        elem.executed = LocalDateTime.now().plus(countTimes, unit)

        return State.NONE
    }

    private fun findLastItem(): Executor? {

        val lastItem = StoreSimple.getLastItemsByState(this)
                ?: StoreSimple.getLastItemsNoneState(this)

        if(lastItem?.state == State.OK) return null // cancel create new elem if exists execute elem

        if(!isLastPeriodEnd(lastItem)) return null

        if(lastItem?.state != State.NONE) {
            createNewElem( getExecutedTime(lastItem) )
        }

        return this
    }

    private fun createNewElem(executed: LocalDateTime): Elem {
        return Elem(task = this,
                idElem = LocalDateTime.now().atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
                name = "$countTimes - ${unit.name}",
                executed = executed).apply {

            StoreSimple.save(this)
        }
    }

    private fun getExecutedTime(elem: Elem?): LocalDateTime {

        if(elem?.state == State.ERROR) {
            return LocalDateTime.now().plus(countTimes * 60, unit)
        }

        return LocalDateTime.now()
    }

    private fun isLastPeriodEnd(lastItem: Elem?): Boolean {

        if(lastItem == null) return true

        val timePassed = lastItem.getLastTime().until(LocalDateTime.now(), unit)

        return (timePassed >= countTimes)
    }

    private fun Elem.getLastTime(): LocalDateTime = executed ?: created

}