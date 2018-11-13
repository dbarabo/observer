package ru.barabo.observer.config.task.template.periodic

import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreSimple
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

interface Periodical : Executor, ActionTask {

    val unit: ChronoUnit

    var count: Long

    var lastPeriod: LocalDateTime?

    override fun actionTask(): ActionTask = this

    override fun findAbstract(): Executor? = findPeriodItems()

    fun findPeriodItems(): Executor? {

        var periodTime = lastPeriod ?: readLastPeriod()

        if(unit == ChronoUnit.MONTHS) {
            periodTime = periodTime.withDayOfMonth(0)
        }

        if(unit in listOf(ChronoUnit.DAYS, ChronoUnit.MONTHS) ) {
            periodTime = periodTime.withHour(0)
        }

        val timeCount = periodTime.until(LocalDateTime.now(), unit)

        lastPeriod = periodTime

        if(timeCount < count) return null

        val timeNewElem = LocalDateTime.now()

        saveNewElemInStore(timeNewElem)

        lastPeriod = timeNewElem

        return this
    }

    fun saveNewElemInStore(timeNewElem: LocalDateTime): Elem? {

        val elem = Elem(task = this,
                idElem = timeNewElem.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
                name = "$count${unit.name}",
                executed = executedTime(timeNewElem) )

        lastPeriod = timeNewElem

        StoreSimple.save(elem)

        return elem
    }

    private fun executedTime(timeCreated :LocalDateTime) :LocalDateTime? =
            accessibleData.executeWait?.let { timeCreated.plusSeconds(it.seconds) }

    private fun readLastPeriod(): LocalDateTime {

        val lastItem = StoreSimple.getLastItemsNoneState(this)

        return lastItem?.executed ?: lastItem?.created ?: LocalDateTime.MIN
    }
}