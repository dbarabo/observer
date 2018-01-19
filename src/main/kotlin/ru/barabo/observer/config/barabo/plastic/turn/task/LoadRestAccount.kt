package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File

object LoadRestAccount : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val accessibleData: AccessibleData = AccessibleData()

    override fun name(): String = "Загрузка остатков по счетам"

    override fun config(): ConfigTask  = PlasticTurnConfig

    val hCardIn = "H:/КартСтандарт/in"

    override fun processFile(file: File) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}