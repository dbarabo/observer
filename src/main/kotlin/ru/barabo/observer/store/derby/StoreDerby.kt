package ru.barabo.observer.store.derby

import ru.barabo.db.SessionException
import ru.barabo.db.Type
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.GroupElem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.StoreDb
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object StoreDerby : StoreDb<Elem>(DerbyTemplateQuery) {

    private var root :GroupElem = GroupElem()

    private val dataList = ArrayList<Elem>()

    private var actualDate = LocalDate.now()

    init {
        readData()
    }

    @Synchronized
    override fun getRootElem() :GroupElem {
        return root
    }

    private fun simpleSave(item :Elem) {

        item.id?.let { updateSave(item) }?:insertSave(item)
    }

    private val INSERT_ELEM = "insert into ALLDATA (ID_ELEM, NAME, STATE, TASK, PATH, CREATED, EXECUTED, " +
            "ERROR, TARGET, BASE, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

    private fun insertSave(item :Elem) {

        item.id = AfinaQuery.nextSequence().toInt()

        AfinaQuery.execute(INSERT_ELEM, item.paramsElem())
    }

    private fun Elem.paramsElem() :Array<Any?>  = arrayOf(
        idElem?.let { it }?:java.sql.Types.BIGINT,
        name,
        state.ordinal,
        task!!.javaClass.canonicalName,
        path,
        java.sql.Date.from(created.atZone(ZoneId.systemDefault()).toInstant()),
        executed?.let {  java.sql.Date.from(it.atZone(ZoneId.systemDefault()).toInstant()) }?:Type.DATE.clazz,
        error?.let { it }?: Type.STRING.clazz,
        target.let { it }?:Type.STRING.clazz,
        base,
        id
    )

    private val UPDATE_ELEM = "update ALLDATA set ID_ELEM = ?, NAME = ?, STATE = ?, TASK = ?, PATH = ?, " +
            "CREATED = ?, EXECUTED = ?, ERROR = ?, TARGET = ?, BASE = ? where ID = ?"

    private fun updateSave(item :Elem) {
        AfinaQuery.execute(UPDATE_ELEM, item.paramsElem())
    }

    @Throws(SessionException::class)
    override fun save(item :Elem) {

        val oldId = item.id

        super.save(item)
        //simpleSave(item)

        if(oldId == null) {

            synchronized(dataList) {
                dataList.add(item)
            }

            addElemToGroup(item)
        } else {
            moveElemFromGroup(item)
        }

        prepareElements()

        sentInfoRefreshAll()
    }

    @Throws(SessionException::class)
    override fun delete(item :Elem) {

        super.delete(item)

        synchronized(dataList) {
            dataList.remove(item)
        }

        removeElemFromGroup(item)

        sentInfoRefreshAll()
    }

    @Synchronized
    fun addNotExistsByIdElem(item :Elem):Boolean {
        val exist = dataList.firstOrNull { (it.task == item.task) && (it.idElem == item.idElem)}

        if(exist == null) {
            save(item)
        }
        return exist == null
    }

//    @Synchronized
//    fun addNotExistsByIdName(item :Elem, isDuplicateName: Boolean) :Boolean {
//
//        val exist = dataList.firstOrNull { (it.task == item.task) && it.isFindByIdName(item.idElem, item.name, isDuplicateName) }
//
//        if(exist == null) {
//            save(item)
//        }
//
//        return exist == null
//    }

    @Synchronized
    fun existsElem(isContainsTask :(ActionTask?)->Boolean, idElem :Long, name :String, isDuplicateName: Boolean): Boolean {

        return dataList.firstOrNull { isContainsTask(it.task) && it.isFindByIdName(idElem, name, isDuplicateName) } != null
    }


    @Synchronized
    fun getLastItemsNoneState(task : ActionTask, noneState :State = State.ARCHIVE) :Elem? {

        val comparatorElemMaxTime = Comparator<Elem> { x, y ->
            val maxX = x.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                    ?:x.created.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L

            val maxY = y.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                    ?:y.created.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L

            if(maxX > maxY) 1 else -1
        }

        return dataList.filter { it.task == task && it.state != noneState }.maxWith(comparatorElemMaxTime)
    }

    @Synchronized
    fun firstItem(task : ActionTask, state :State = State.NONE, executed :LocalDateTime = LocalDateTime.now(), target :String? = null) :Elem? {
        return dataList.firstOrNull { (it.state == state) && (it.task == task) &&

                (it.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:1_000_000_000_000_000 >
                        executed.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() ) &&

                (target?.let { tar -> tar == it.target  } ?: true )
        }
    }

    @Synchronized
    fun getItems(state :State = State.NONE, executed :LocalDateTime = LocalDateTime.now(), isContainsTask :(ActionTask?)->Boolean) :List<Elem>
          = dataList.filter { it.state == state &&
            isContainsTask(it.task) &&
            (it.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?: Long.MAX_VALUE <
                    executed.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) }



    @Synchronized
    private fun removeElemFromGroup(item :Elem) {

        val config = root.child.firstOrNull { it.elem.task?.config() == item.task?.config() } ?: return

        val taskGroup = config.child.firstOrNull {it.elem.task == item.task &&
                ((it.elem.id == item.id) || (it.child.firstOrNull {it.elem.id == item.id} != null) )} ?: return

        if(taskGroup.child.size == 0) {
            config.removeTask(taskGroup)
            return
        }

        val itemGroup = taskGroup.child.first {it.elem.id == item.id}

        taskGroup.removeTask(itemGroup)
    }

    @Synchronized
    private fun addElemToGroup(elem :Elem) {

        val config = root.child.firstOrNull { it.elem.task?.config() == elem.task?.config() }

        if(config == null) {

            root.addConfig(elem).addElemTask(elem)
            return
        }

        val groupTask = config.child.firstOrNull { (it.elem.task == elem.task) && (it.elem.state == elem.state) }

        if(groupTask == null) {
            config.addElemTask(elem)
            return
        }

        if(groupTask.child.isEmpty()) {

            groupTask.reverseSingleElemToGroup()
        }

        groupTask.addElemTask(elem)
    }

    @Synchronized
    private fun moveElemFromGroup(item :Elem) {
        removeElemFromGroup(item)

        addElemToGroup(item)
    }

    @Synchronized
    fun checkDate(dateCheck :LocalDate) {
        if(actualDate.dayOfYear !=  dateCheck.dayOfYear) {
            readData(dateCheck)
        }
    }

    @Synchronized
    fun readData(dateCheck :LocalDate = LocalDate.now()) {

        actualDate = dateCheck

        synchronized(dataList){
            dataList.clear()
        }
        synchronized(root) {
            root = GroupElem()
        }

        template.select(Elem::class.java) {
            _ :Elem?, row :Elem ->

            synchronized(dataList){
                dataList.add(row)
            }

            this.addElemToGroup(row)
        }

        prepareElements()

        sentInfoRefreshAll()
    }

    private fun prepareElements() {

        val comparatorDateTime = object : Comparator<LocalDateTime> {
            override fun compare(x : LocalDateTime, y: LocalDateTime) =
                    if(x.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L >
                    y.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L) 1 else -1
        }

        val comparatorDateTimeNull = object : Comparator<LocalDateTime?> {
            override fun compare(x : LocalDateTime?, y: LocalDateTime?) =
                    if(x?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L >
                            y?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L) 1 else -1
        }

        root.child.forEach { config ->
            config.child.forEach { group ->
                if(!group.child.isEmpty()) {
                    group.elem.created = group.child.map { it.elem.created }.minWith(comparatorDateTime)?:group.elem.created

                    group.elem.executed = group.child.map { it.elem.executed }.maxWith(comparatorDateTimeNull)
                }
            }
        }
    }

    private fun LocalDateTime.minLocalDate(val1 :LocalDateTime) :LocalDateTime {
        return if(val1.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L >
                this.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L)this else val1
    }

    fun maxLocalDate(val1 :LocalDateTime?, val2 :LocalDateTime?) :LocalDateTime? =
        if(val1?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L >
                val2?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L)val1 else val2

}