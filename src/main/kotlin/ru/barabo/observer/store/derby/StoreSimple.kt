package ru.barabo.observer.store.derby

import javafx.application.Platform
import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.info.InfoHtmlData
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.StoreDb
import tornadofx.observable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object StoreSimple : StoreDb<Elem, TreeElem>(DerbyTemplateQuery) {

    override fun getRootElem(): TreeElem = root

    private var root :TreeElem = TreeElem(group = TreeGroup())

    private val dataList = ArrayList<Elem>()

    private var actualDate = LocalDate.now()

    init {
        StoreSimple.readData()
    }

    val logger = LoggerFactory.getLogger(StoreSimple::class.java)!!

    @Synchronized
    fun addNotExistsByIdElem(item :Elem, stateFind: State? = null): Boolean {
        val exist = dataList.firstOrNull {
            ((it.task == item.task) && (it.idElem == item.idElem)) &&
            (stateFind?.let { st -> it.state == st } ?: true ) }

        if(exist == null) {
            save(item)
        }
        return exist == null
    }

    @Synchronized
    fun findElemByFile(name: String, path: String, task: ActionTask) : Elem? =
            dataList.firstOrNull {(it.task == task) && (it.name == name) && (it.path == path) }

    @Synchronized
    fun findElemById(idElem: Long, task: ActionTask): Elem? = dataList.firstOrNull {(it.task == task) && (it.idElem == idElem)}


    fun existsElem(isContainsTask :(ActionTask?)->Boolean, idElem :Long, name :String, isDuplicateName: Boolean): Boolean =
        synchronized(dataList) {
            dataList.firstOrNull { isContainsTask(it.task) && it.isFindByIdName(idElem, name, isDuplicateName) } != null
        }

    @Synchronized
    fun getLastItemsNoneState(task: ActionTask, noneState: State = State.ARCHIVE) :Elem? {

        val comparatorElemMaxTime = comparatorElemByExecTime()

        return dataList.filter { it.task === task && it.state != noneState }.maxWith(comparatorElemMaxTime)
    }

    @Synchronized
    fun getLastItemByState(task: ActionTask, findState: State = State.NONE): Elem? {

        val comparatorElemMaxTime = comparatorElemByExecTime()

        return dataList.filter { it.task === task && it.state == findState }.maxWith(comparatorElemMaxTime)
    }

    private fun comparatorElemByExecTime(): Comparator<Elem> = Comparator<Elem> { x, y ->
        val maxX = x.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                ?:x.created.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L

        val maxY = y.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                ?:y.created.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L

        if(maxX > maxY) 1 else -1
    }


    @Synchronized
    fun firstItem(task : ActionTask, state :State = State.NONE, executed :LocalDateTime = LocalDateTime.now(), target :String? = null) :Elem? {
        return dataList.firstOrNull { (it.state == state) && (it.task == task) &&

                (it.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:Long.MAX_VALUE >
                        executed.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() ) &&

                (target?.let { tar -> tar == it.target  } ?: true )
        }
    }

    @Synchronized
    fun getItems(state :State = State.NONE, executed :LocalDateTime = LocalDateTime.now(), isContainsTask :(ActionTask?)->Boolean) :List<Elem>
            = dataList.filter { it.state === state &&
            isContainsTask(it.task) &&
            (it.executed?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?: Long.MAX_VALUE <
                    executed.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) }

    @Synchronized
    fun findFirstByConditionName(state: State = State.OK, task: ActionTask, isConditionName: (String)-> Boolean) :Elem? =
        dataList.firstOrNull { it.state == state && it.task === task && isConditionName(it.name) }


    @Synchronized
    fun checkDate(dateCheck: LocalDate) {
        if(actualDate.dayOfYear !=  dateCheck.dayOfYear) {

            InfoHtmlData.sendInfo(actualDate, root)

            Platform.runLater { run {
                readData(dateCheck)
            } }
        }
    }

    @Throws(SessionException::class)
    override fun save(item :Elem) {

        val oldId = item.id

        super.save(item)
        //simpleSave(item)

        if(oldId == null) {
            synchronized(dataList) { dataList.add(item) }
        }

        updateTreeElem(item, oldId == null)

        sentInfoRefreshAll()
    }

    private fun updateTreeElem(item: Elem, isAdd: Boolean) {

        Platform.runLater { run {

            val group = if(isAdd) addElemToGroup(item) else checkMoveElemFromGroup(item)

            group.group?.let { synchronized(it){ it.prepareTaskGroup() } }
        } }
    }

    @Synchronized
    private fun checkMoveElemFromGroup(elem :Elem):TreeElem {

        val config = root.group!!.childs.first { it.group!!.config === elem.task!!.config() }

        val taskGroup = config.group!!.findFirstGroupByTaskState(elem.task, elem.state)

        return processRootElem(config, elem, taskGroup) ?: processTaskGroup(config, elem, taskGroup)
    }

    private fun findSource(config: TreeElem, elem: Elem): Pair<TreeElem, TreeElem> {

        var oldTaskGroup: TreeElem? = null

        var findItem: TreeElem? = null

        run find@{
             config.group!!.childs.forEach {

                findItem = it.group?.childs?.firstOrNull { it.elem === elem }

                if(findItem != null) {
                    oldTaskGroup = it
                    return@find
                }
            }
        }

        return Pair(oldTaskGroup!!, findItem!!)
    }

    private fun processTaskGroup(config: TreeElem, elem: Elem, newTaskGroup: TreeElem?) :TreeElem {

        val (oldTaskGroup, findItem) = findSource(config, elem)

        if(newTaskGroup?.group?.taskGroup?.task === oldTaskGroup.group?.taskGroup?.task &&
                newTaskGroup?.group?.taskGroup?.state === oldTaskGroup.group?.taskGroup?.state) return oldTaskGroup

        oldTaskGroup.removeChild(findItem, config)

        return newTaskGroup?.apply{ group?.childs?.add(findItem) }
                ?: config.addTaskGroup(elem).apply { group?.childs?.add(findItem) }
    }

    private fun processRootElem(config: TreeElem, elem: Elem, taskGroup: TreeElem? ): TreeElem? {

        val singleElem = config.group!!.childs.firstOrNull { it.elem === elem } ?: return null

        return taskGroup?.apply {
                config.group!!.childs -= singleElem

                group!!.childs += singleElem

            } ?: config.addTaskGroup(elem).apply {

                if(this !== config) {
                    config.group!!.childs -= singleElem

                    group!!.childs += singleElem
                }
            }
    }

    @Synchronized
    private fun readData(dateCheck : LocalDate = LocalDate.now()) {
        actualDate = dateCheck

        synchronized(dataList) {
            dataList.clear()
        }
        synchronized(root.group!!.childs) {
            root.group!!.childs.removeAll(root.group!!.childs)
        }

        template.select(Elem::class.java) { _: Elem?, row: Elem ->

            synchronized(dataList) { dataList.add(row) }

            this.addElemToGroup(row)
        }

        root.group!!.childs.forEach { it.group?.prepareAllTaskForConfig() }
    }

    @Synchronized
    private fun addElemToGroup(elem: Elem) :TreeElem {

        val config = root.group!!.childs.firstOrNull { it.group!!.config === elem.task!!.config() }
                ?: root.addConfig(elem.task!!.config())

        val taskGroup = config.group?.findFirstGroupByTaskState(elem.task, elem.state)
                ?: config.addTaskGroup(elem)

        taskGroup.group!!.childs += TreeElem(elem)

        return taskGroup
    }
}

class TreeElem(var elem: Elem? = null,
               var group: TreeGroup? = null) {

    val task :String get() = elem?.task?.name() ?: (group?.config?.name()) ?: (group?.taskGroup?.task?.name()) ?: "!"

    val name :String get() = elem?.name ?: ""

    val state :String get() = elem?.state?.label ?: (group?.taskGroup?.state?.label) ?:""

    val id: String get() = elem?.idElem?.toString() ?: ""

    val created: String get() = elem?.created?.formatter()
            ?: group?.taskGroup?.created?.formatter() ?: ""

    val executed: String get() = elem?.executed?.formatter()
            ?: group?.taskGroup?.executed?.formatter() ?: ""

    val count: String get() = group?.childs?.size?.toString() ?: ""

    val error :String get() = elem?.error?:""

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM HH:mm")

    private fun LocalDateTime.formatter(): String = if(dayOfYear == LocalDateTime.now().dayOfYear)
        timeFormatter.format(this) else dateTimeFormatter.format(this)


    fun removeChild(delItem: TreeElem, parent: TreeElem) {

        group!!.childs -= delItem

        if(group!!.childs.isEmpty()) {
            parent.group!!.childs -= this
        }
    }

    fun addConfig(config: ConfigTask): TreeElem {

        val groupConfig = TreeElem(group = TreeGroup(config))

        group!!.childs += groupConfig

        return groupConfig
    }

    fun addTaskGroup(elem: Elem): TreeElem {

       val singleChild = this.group!!.findFirstElemByTaskState(elem.task, elem.state, elem)

        return singleChild?. let {
            val taskGroup = TreeElem(null,
                    TreeGroup(null, TaskGroup(elem.task!!, elem.state, elem.created, elem.executed) ))

            this.group!!.childs -= it

            this.group!!.childs += taskGroup

            taskGroup.group!!.childs += it

            taskGroup

        } ?: this
    }
}

class TreeGroup(val config: ConfigTask? = null,
                val taskGroup: TaskGroup? = null,
                val childs: MutableList<TreeElem> = ArrayList<TreeElem>().observable()) {

    fun findFirstElemByTaskState(task: ActionTask?, state: State, except: Elem? = null): TreeElem? =
        childs.firstOrNull {it.elem?.task === task && it.elem?.state === state && except !== it.elem}

    fun findFirstGroupByTaskState(task: ActionTask?, state: State): TreeElem? =
            childs.firstOrNull {it.group?.taskGroup?.task === task && it.group?.taskGroup?.state === state }

    fun prepareAllTaskForConfig() {
        childs.forEach {
            it.group?.prepareTaskGroup()
        }
    }

    fun prepareTaskGroup() {

        val comparatorDateTimeNull = Comparator<LocalDateTime?> { x, y ->
            if(x?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L >
                    y?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?:0L) 1 else -1
        }

        taskGroup?.created = childs.map { it.elem?.created }.minWith(comparatorDateTimeNull)?: LocalDateTime.MIN

        taskGroup?.executed = childs.map { it.elem?.executed }.maxWith(comparatorDateTimeNull)?: LocalDateTime.MIN
    }
}

class TaskGroup(val task: ActionTask,
                val state: State,
                var created: LocalDateTime,
                var executed: LocalDateTime?)