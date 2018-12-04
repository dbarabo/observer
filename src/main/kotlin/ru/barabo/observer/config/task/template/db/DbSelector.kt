package ru.barabo.observer.config.task.template.db

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreSimple

interface DbSelector : Executor {

    val select :String

    fun params() :Array<Any?>? = null

    fun isCursorSelect(): Boolean = false

    fun actionTask(selectorValue :Any?) :ActionTask

    override fun findAbstract() :Executor? = findDbItems()

    private fun findDbItems() :Executor?  {

        val data = if(isCursorSelect() ) AfinaQuery.selectCursor(select, params())
                         else AfinaQuery.select(select, params())

        val count = data.map {
            Elem(it[0] as Number?,
                    if(it.size < 2)null else it[1] as String?,
                    actionTask(if(it.size < 3)null else it[2]),
                    accessibleData.executeWait) }
                .filter { StoreSimple.addNotExistsByIdElem(it) }.count()

        return if(count == 0)null else this
    }
}
