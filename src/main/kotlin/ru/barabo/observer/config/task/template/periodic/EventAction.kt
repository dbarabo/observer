package ru.barabo.observer.config.task.template.periodic

import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreSimple

data class IdName(val id: Long = 0L, val name: String = "")

val NULL_ID_NAME = IdName()

interface EventAction : Executor, ActionTask {

    override fun actionTask(): ActionTask = this

    override fun findAbstract(): Executor? = findEventAction()

    fun getEventActionNow(): IdName

    var actionIdNow: IdName

    override fun isAccess() = super.isAccess() && isEventNow()

    private fun isEventNow(): Boolean {

        actionIdNow = getEventActionNow()

        return actionIdNow.id != 0L
    }

    private fun findEventAction(): Executor? {

        val elemFind = StoreSimple.findElemById(actionIdNow.id, this)

        if(elemFind != null) return null

        val newElem = Elem(task = this,
                idElem = actionIdNow.id,
                name = actionIdNow.name,
                executed = executedTime() )

        StoreSimple.save(newElem)

        return this
    }
}