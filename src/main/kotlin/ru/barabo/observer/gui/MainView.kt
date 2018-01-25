package ru.barabo.observer.gui

import com.sun.javafx.tk.Toolkit
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
import ru.barabo.observer.store.GroupElem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.StoreListener
import ru.barabo.observer.store.TaskMapper
import ru.barabo.observer.store.derby.StoreDerby
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

class MainView: View(), StoreListener {

    override var root : VBox = VBox()

    private var treeTable :TreeTableView<GroupElem>? = null

    private val actionMenu = ActionMenu()

    private val menuBar = MenuBar()

    init {
        title = "Наблюдатель: [${afinaTitle()}] Сборка:${TaskMapper.build()}"

        menuItems(menuBar, menuBar.menu("Запустить", graphic = ResourcesManager.icon("play.png")),true)

        menuItems(menuBar, menuBar.menu("Приостановить", graphic = ResourcesManager.icon("pause.png")),false)

        menuBar += actionMenu

        root += menuBar

        StoreDerby.addStoreListener(this)
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

    override fun refreshAll(rootElem: GroupElem) {

        if(treeTable?.root?.value === rootElem) {
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

                treeTable?.root?.children?.forEach {
                    it?.children?.forEach { it.expandedProperty().set(false) }
                }

                val fontMetrics = Toolkit.getToolkit().fontLoader.getFontMetrics(treeTable?.columns?.get(0)?.label()?.font)

                val defaultValues = arrayOf("Проверка отправки ПТК ПСД", "В Архиве",
                        "KESDT_0021_0000_20171227_001.ARJ", "1171576789", "23:59:59", "23:59:59", "9999", "Нет ошибок Да")

                defaultValues.indices.forEach {
                    treeTable?.columns?.get(it)?.prefWidth = fontMetrics.computeStringWidth(defaultValues[it]).toDouble() + 5
                }

                // forEach {it.prefWidth = fontMetrics.computeStringWidth(it.text).toDouble() + 10 }
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

private fun getFontColor(state :State, isConfig :Boolean) :Color {

    if(isConfig) return Color.BLACK

    return when(state){
        State.OK -> Color.GRAY
        State.ERROR -> Color.RED
        State.ARCHIVE -> Color.LIGHTGRAY
        else -> Color.BLACK
    }
}

private fun getColorBackGround(isConfig :Boolean, isSelected :Boolean) :String {

    return if(isConfig) "-fx-background-color: lightgray;" else "-fx-background-color: white;"
}

private fun getFontStyle(isEmptyChild :Boolean) :String {
    return if(isEmptyChild) "" else "-fx-font-weight: bold;"
}

private fun treeTable(rootGroup :GroupElem) :TreeTableView<GroupElem> {
    return TreeTableView<GroupElem>().apply {

        root = TreeItem(rootGroup)

        column("Задача", GroupElem::task)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it
//
//            treeTableView.selectionModel.focusedIndex
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style += getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig, isSelected)
//        }

        column("Статус", GroupElem::state)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style = getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig, treeItem.value == treeTableView.selectedItem)
//        }

        column("Имя", GroupElem::name)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style = getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig, treeItem.value == treeTableView.selectedItem)
//        }

        column("id", GroupElem::id)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style = getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig, treeItem.value == treeTableView.selectedItem)
//        }

        column("Создан", GroupElem::created)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style = getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig, treeItem.value == treeTableView.selectedItem)
//        }

        column("Обработан", GroupElem::executed)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style = getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig,  treeItem.value == treeTableView.selectedItem )
//        }

        column("Кол-во", GroupElem::count)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it?.toString()
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style = getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig, treeItem.value == treeTableView.selectedItem)
//        }

        column("Ошибка", GroupElem::error)
//                .cellFormat {
//            val treeItem = treeTableView.getTreeItem(index)?:TreeItem<GroupElem>(GroupElem())
//
//            text = it
//
//            textFill = getFontColor(treeItem.value.elem.state, treeItem.value.isConfig)
//
//            style = getFontStyle(treeItem.value.child.isEmpty())
//
//            style += getColorBackGround(treeItem.value.isConfig, treeItem.value == treeTableView.selectedItem)
//        }

        populate { it.value.child }

        root.isExpanded = true

        this.isShowRoot = false

        this.resizeColumnsToFitContent()
    }
}
