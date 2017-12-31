package ru.barabo.observer.config.task.template.periodic

import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreDerby
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

interface Periodical : Executor, ActionTask {

    val unit :ChronoUnit

    val count :Long

    var lastPeriod :LocalDateTime?

    override fun actionTask() :ActionTask = this

    override fun findAbstract() :Executor? = findPeriodItems()

    private fun findPeriodItems() :Executor? {

        lastPeriod = lastPeriod?.let { lastPeriod }?: readLastPeriod()

        val timeCount = lastPeriod!!.until(LocalDateTime.now(), unit)

        if(timeCount < count) return null

        val timeNewElem = LocalDateTime.now()

        val elem = Elem(task = this,
                   idElem = timeNewElem.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
                   name = "$count${unit.name}",
                   executed = executedTime(timeNewElem) )

        lastPeriod = timeNewElem

        StoreDerby.save(elem)

        return this
    }

    private fun executedTime(timeCreated :LocalDateTime) :LocalDateTime? =
            if(accessibleData.executeWait == null) null
               else { timeCreated.plusSeconds(accessibleData.executeWait?.seconds?:0) }



    private fun readLastPeriod() :LocalDateTime {

        val lastItem = StoreDerby.getLastItemsNoneState(this)

        return lastItem?.executed?.let { lastItem.executed }?:lastItem?.created
                ?: LocalDateTime.MIN
    }
}