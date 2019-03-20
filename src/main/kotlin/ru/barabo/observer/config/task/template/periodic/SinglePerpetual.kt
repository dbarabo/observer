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

    private fun findLastItem(): Executor? {

        val lastItem = StoreSimple.getLastItemsNoneState(this)

        if(!isLastPeriodEnd(lastItem)) return null

        val nextItem = if(lastItem?.state == State.NONE) lastItem else createNewElem(lastItem)

        nextItem.executed = LocalDateTime.now().plus(countTimes, unit) // getNextTimeByExec(nextItem)

        return this
    }

    private fun createNewElem(lastItem: Elem?): Elem {
        return Elem(task = this,
                idElem = LocalDateTime.now().atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
                name = "$countTimes - ${unit.name}",
                executed = lastItem?.executed ?: lastItem?.created ).apply {

            StoreSimple.save(this)
        }
    }

    private fun getNextTimeByExec(elem: Elem): LocalDateTime {

        val priorTime = elem.executed ?: elem.created

        return priorTime.plus(countTimes, unit)
    }

    private fun isLastPeriodEnd(lastItem: Elem?): Boolean {

        if(lastItem == null) return true

        val timePassed = lastItem.getLastTime().until(LocalDateTime.now(), unit)

        return (timePassed >= countTimes)
    }

    private fun Elem.getLastTime(): LocalDateTime = executed ?: created

}