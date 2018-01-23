package ru.barabo.observer.store

import org.slf4j.LoggerFactory
import tornadofx.observable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class GroupElem (var elem :Elem = Elem(),
                      private var parentino : GroupElem? = null,
                      val isConfig :Boolean = false,
                      val child: MutableList<GroupElem> = ArrayList<GroupElem>().observable() ) {

    private val logger = LoggerFactory.getLogger(GroupElem::class.java)

    val task :String? get() = if(isConfig) elem.task?.config()?.name() else elem.task?.name()

    val state :String get() = if(isConfig) "" else elem.state.label

    val name :String get() = if(!child.isEmpty()) "" else elem.name

    val id :String get() = if(!child.isEmpty()) "" else elem.idElem?.toString()?:""

    val created :String get() = if(isConfig) "" else (if(elem.created.dayOfYear == LocalDateTime.now().dayOfYear) {
        DateTimeFormatter.ofPattern("HH:mm:ss").format(elem.created)
    } else {
        DateTimeFormatter.ofPattern("MM dd HH:mm:ss").format(elem.created)
    })

    val executed :String? get() = if(isConfig) "" else if(elem.executed != null)DateTimeFormatter.ofPattern("HH:mm:ss").format(elem.executed) else null

    val count :Int? get() = if(isConfig) null else child.size

    val error :String?  get() = if(!child.isEmpty()) null else elem.error

    @Synchronized
    fun removeTask(item :GroupElem) {
        child.remove(item)

        if (parentino == null || child.size != 0) {
            return
        }

        parentino?.child?.remove(this)

        if (parentino?.parentino == null || parentino?.child?.size != 0) {
            return
        }

        val root = parentino?.parentino

        root?.child?.remove(parentino!!)
    }


    /**
     * меняет у одиночн. Task Э Config, эл-т, так что будет группа,
     * @return этот же эл-т, но он уже будет группой
     */
    @Synchronized
    fun reverseSingleElemToGroup() :GroupElem {

        val rowGroup = this.elem.copy(id = null)

        val taskChild = GroupElem(this.elem, this)

        this.elem = rowGroup

        this.child.add(taskChild)

        return this
    }

    fun addElemTask(row :Elem) :GroupElem {
        val taskGroup = GroupElem(row, this)

        synchronized(child) {
            child.add(taskGroup)
        }

        return taskGroup
    }

    fun addConfig(row :Elem) :GroupElem {
        val configGroup = GroupElem(row.copy(id=null), this, isConfig = true)

        synchronized(child) {
            child.add(configGroup)
        }

        return configGroup
    }
}