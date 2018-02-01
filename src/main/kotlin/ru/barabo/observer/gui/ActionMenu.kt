package ru.barabo.observer.gui

import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.resources.ResourcesManager
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import ru.barabo.observer.store.derby.TreeElem
import tornadofx.plusAssign
import java.util.*

class ActionMenu : Menu("Действия", ResourcesManager.icon("action.png")) {
    var groupElem : TreeElem? = null
        set(value) {
            field = value

            this.items.clear()

            value?.getActionMenuItems()?.forEach {

                val menuItem = MenuItem(it.first)
                menuItem.setOnAction{_ -> it.second() }
                this += menuItem
            }
        }
}

private fun  TreeElem.getActionMenuItems() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

   elem?.apply {

       list.addAll(defaultItemsByState(state) )

       list.addAll(itemsByTask(task!!) )
   }

    return list
}


private fun TreeElem.itemsByTask(task : ActionTask) :List<Pair<String, ()->Unit>> {
    return emptyList()
}

private fun TreeElem.defaultItemsByState(state :State) :List<Pair<String, ()->Unit>> {

    val list = ArrayList<Pair<String, ()->Unit>>()
    list.addAll(
    when(state) {
        State.NONE -> defaultItemsByNoneState()
        State.OK -> defaultItemsByOkState()
        State.ERROR -> defaultItemsByErrorState()
        State.ARCHIVE -> defaultItemsByArchiveState()
        else -> emptyList()
    })

    return list
}

private fun TreeElem.defaultItemsByNoneState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("Исполнить", this::toExecute))

    list.add(Pair("В Архив", this::toArchiveState))

    return list
}

private fun TreeElem.defaultItemsByOkState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("В <НЕ Исполнен>", this::toStateNone))

    return list
}

private fun TreeElem.defaultItemsByErrorState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("Попытаться исполнить", this::toExecute))

    list.add(Pair("В Архив", this::toArchiveState))

    return list
}

private fun TreeElem.defaultItemsByArchiveState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("В <НЕ Исполнен>", this::toStateNone))

    return list
}

@Synchronized
private fun TreeElem.toArchiveState() {

    elem?.apply {
        state = State.ARCHIVE
        error = error?.replace("\n", " ")

        StoreSimple.save(this)
    }
}

@Synchronized
private fun TreeElem.toStateNone() {

    elem?.apply {
        state = State.NONE
        executed = null

        StoreSimple.save(this)
    }
}

@Synchronized
private fun TreeElem.toExecute() {

    elem?.apply {
        if(task is Executor) (task as Executor).executeElem(this)
    }
}

