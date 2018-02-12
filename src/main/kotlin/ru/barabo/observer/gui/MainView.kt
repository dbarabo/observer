package ru.barabo.observer.gui

import javafx.application.Platform
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Screen
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.resources.ResourcesManager
import ru.barabo.observer.store.State
import ru.barabo.observer.store.StoreListener
import ru.barabo.observer.store.TaskMapper
import ru.barabo.observer.store.derby.StoreSimple
import ru.barabo.observer.store.derby.TreeElem
import tornadofx.*


fun startLaunch(args :Array<String>) = launch<MainApp>(args)

class MainApp: App(MainView::class) {

    val logger = LoggerFactory.getLogger(MainApp::class.java)!!

    override fun start(stage: Stage) {

        super.start(stage)

        val screen = Screen.getPrimary()

        stage.icons.add(ResourcesManager.image("observer.png"))

        stage.x = screen.visualBounds.minX
        stage.y = screen.visualBounds.minY

        stage.width = screen.visualBounds.width / 3 * 2

        stage.height = screen.visualBounds.height
    }

    override fun stop() {

        TaskMapper.stopConfigList()

        super.stop()
    }
}

class MainView: View(), StoreListener<TreeElem> {

    override var root : VBox = VBox()

    private var treeTable :TreeTableView<TreeElem>? = null

    private val actionMenu = ActionMenu()

    private val menuBar = MenuBar()

    init {
        title = "Наблюдатель: [${afinaTitle()}] Сборка:${TaskMapper.build()}"

        menuItems(menuBar, menuBar.menu("Запустить", graphic = ResourcesManager.icon("play.png")),true)

        menuItems(menuBar, menuBar.menu("Приостановить", graphic = ResourcesManager.icon("pause.png")),false)

        menuBar += actionMenu

        root += menuBar

        StoreSimple.addStoreListener(this)
    }

    private fun afinaTitle() = if(TaskMapper.isAfinaBase())"AFINA" else "TEST"

    private fun menuItems(menuBar :MenuBar, menu :Menu, isStart :Boolean) :Menu  {

        val allItem  = MenuItem("ВСЕ")

        allItem.setOnAction { _ -> TaskMapper.runConfigList() }

        menu += allItem

        menu.addChildIfPossible(Separator())

        menu.items.addAll( TaskMapper.configList().map{ MenuItemConfigStartStop(menuBar, it, isStart) })

        return menu
    }

    override fun refreshAll(rootElem: TreeElem) {

        if(treeTable != null) {
            return
        }

        Platform.runLater({
            run {
                treeTable?.removeFromParent()

                synchronized(rootElem) {
                    treeTable = treeTable(rootElem)
                }

                treeTable?.root?.children?.forEach {
                    it.expandedProperty().set(true)
                    it?.children?.forEach { it.expandedProperty().set(true) }
                }

                treeTable?.selectionModel?.selectedItemProperty()?.addListener(
                        { _, _, newSelection ->

                            actionMenu.groupElem = newSelection?.value
                        })

                root += treeTable!!

                VBox.setVgrow(treeTable, Priority.ALWAYS)

                this.treeTable?.root?.children?.forEach {
                    it?.children?.forEach { it.expandedProperty().set(false) }
                }

//                val fontMetrics = Toolkit.getToolkit().fontLoader.getFontMetrics(treeTable?.columns?.get(0)?.label()?.font)
//
//                val defaultValues = arrayOf("Проверка отправки ПТК ПСД", "В Архиве",
//                        "KESDT_0021_0000_20171227_001.ARJ", "1171576789", "23:59:59", "23:59:59", "9999", "Нет ошибок Да")
//
//                defaultValues.indices.forEach {
//                    treeTable?.columns?.get(it)?.prefWidth = fontMetrics.computeStringWidth(defaultValues[it]).toDouble() + 5
//                }

            }
        })
    }
}

class MenuItemConfigStartStop(private val menuBar :MenuBar,
                              private val config : ConfigTask,
                              private val isStart :Boolean) :MenuItem() {

    init {
        this.text = config.name()

        isDisable =  isDisabled()

        if(isStart) this.setOnAction{_ ->
            config.starting()
            checkDisabledChildItem()
        } else  this.setOnAction{_ ->
            config.stoping()
            checkDisabledChildItem()
        }
    }

    private fun isDisabled() = if(isStart) config.isRun() else config.isRun().not()

    private fun checkDisabledChildItem() {
        menuBar.menus.forEach {
            it.items.forEach { if(it is MenuItemConfigStartStop) it.isDisable =  it.isDisabled() }
        }
    }
}

private fun colorByElem(elem: TreeElem?): Color {

    val state = elem?.elem?.state ?: elem?.group?.taskGroup?.state

    return when (state) {
        State.NONE->Color.DARKBLUE
        State.OK -> Color.BLACK
        State.ERROR -> Color.RED
        State.ARCHIVE -> Color.GRAY
        State.PROCESS -> Color.YELLOWGREEN
        else -> Color.BLACK
    }
}

private fun backGroundColorRow(elem: TreeElem?): Pair<Color, String> {

    val color = colorByElem(elem)

    val backGround = if(elem?.group?.config != null)
        "-fx-background-color:lightgray;-fx-font-weight: bold;"
        else "-fx-background-color:white;"

    return Pair(color, backGround)
}

private fun treeTable(rootGroup :TreeElem) :TreeTableView<TreeElem> {

   val cellRowFormatter :(TreeTableCell<*, *>.(Any?) -> Unit) = {
       val elem = this.treeTableRow?.treeItem?.value

       if(elem is TreeElem?) {

            val (color, background) = backGroundColorRow(elem)

            //this.treeTableRow?.style = background

            //this.treeTableRow?.textFill = color

            this.textFill = color

            this.style = background

            this.text = this.item as? String
        }
   }

   return TreeTableView<TreeElem>().apply {

       this.style = ".tree-table-row-cell:selected { -fx-text-background-color:green; -fx-background-color: steelblue; -fx-selection-bar: steelblue;} " +
               ".tree-table-row-cell:selected .text { -fx-fill: green; }"

        root = TreeItem(rootGroup)

        column("Задача", TreeElem::task).cellFormat(formatter = cellRowFormatter)

        column("Имя", TreeElem::name).cellFormat(formatter = cellRowFormatter)

        column("Статус", TreeElem::state).cellFormat(formatter = cellRowFormatter)

        column("id", TreeElem::id).cellFormat(formatter = cellRowFormatter)

        column("Создан", TreeElem::created).cellFormat(formatter = cellRowFormatter)

        column("Обработан", TreeElem::executed).cellFormat(formatter = cellRowFormatter)

        column("Кол-во", TreeElem::count).cellFormat(formatter = cellRowFormatter)

        column("Ошибка", TreeElem::error)

        populate { it.value.group?.childs }

        root.isExpanded = true

        this.isShowRoot = false

        this.resizeColumnsToFitContent()
    }
}
