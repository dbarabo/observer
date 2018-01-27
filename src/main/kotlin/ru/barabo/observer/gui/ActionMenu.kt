package ru.barabo.observer.gui

import javafx.scene.control.Menu
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.resources.ResourcesManager
import ru.barabo.observer.store.GroupElem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreDerby
import ru.barabo.observer.store.derby.TreeElem
import java.util.*

class ActionMenu : Menu("Действия", ResourcesManager.icon("action.png")) {
    var groupElem : TreeElem? = null
        set(value) {
            field = value

            this.items.clear()

//            value?.getActionMenuItems()?.forEach {
//
//                val menuItem = MenuItem(it.first)
//                menuItem.setOnAction{_ -> it.second() }
//                this += menuItem
//            }
        }
}

private fun  GroupElem.getActionMenuItems() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    if(this.isConfig) {
        list.addAll(refreshTable() )
    } else {
        list.addAll(defaultItemsByState(elem.state) )

        list.addAll(itemsByTask(elem.task!!) )
    }

    return list
}

private fun GroupElem.refreshTable(): List<Pair<String, () -> Unit>> =
        listOf(Pair("Обновить", { StoreDerby.readData() } ))



private fun GroupElem.itemsByTask(task : ActionTask) :List<Pair<String, ()->Unit>> {
    return emptyList()
}

private fun GroupElem.defaultItemsByState(state :State) :List<Pair<String, ()->Unit>> {

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

private fun GroupElem.defaultItemsByNoneState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("Исполнить", this::toExecute))

    list.add(Pair("В Архив", this::toArchiveState))

    return list
}

private fun GroupElem.defaultItemsByOkState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("В <НЕ Исполнен>", this::toStateNone))

    return list
}

private fun GroupElem.defaultItemsByErrorState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("Попытаться исполнить", this::toExecute))

    list.add(Pair("В Архив", this::toArchiveState))

    return list
}

private fun GroupElem.defaultItemsByArchiveState() :List<Pair<String, ()->Unit>> {
    val list = ArrayList<Pair<String, ()->Unit>>()

    list.add(Pair("В <НЕ Исполнен>", this::toStateNone))

    return list
}

@Synchronized
private fun GroupElem.toArchiveState() {
    elem.state = State.ARCHIVE
    elem.error = elem.error?.replace("\n", " ")
    StoreDerby.save(elem)
}

@Synchronized
private fun GroupElem.toStateNone() {
    elem.state = State.NONE
    elem.executed = null

    StoreDerby.save(elem)
}

@Synchronized
private fun GroupElem.toExecute() {

    if(elem.task is Executor) {
        (elem.task as Executor).executeElem(elem)
    }
}

