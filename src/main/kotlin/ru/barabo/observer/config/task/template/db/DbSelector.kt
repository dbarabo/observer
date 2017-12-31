package ru.barabo.observer.config.task.template.db

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreDerby

interface DbSelector : Executor {

    val select :String

    fun params() :Array<Any?>? = null

    fun actionTask(selectorValue :Any?) :ActionTask

    override fun findAbstract() :Executor? = findDbItems()

    private fun findDbItems() :Executor?  {

        val count = AfinaQuery.select(select, params()).map {
            Elem(it[0] as Number?,
                    if(it.size < 2)null else it[1] as String?,
                    actionTask(if(it.size < 3)null else it[2]),
                    accessibleData.executeWait) }
                .filter { StoreDerby.addNotExistsByIdElem(it) }.count()

        return if(count == 0)null else this
    }
}
