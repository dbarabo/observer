package ru.barabo.observer.config.task.template.file

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

abstract class FileMoverRegexp(val pathFind: String,
                               val maskFromPathsTo: Map<String, ()->String>,
                               val isMove: Boolean) : ActionTask, Executor, FileFinder {


    override val fileFinderData: List<FileFinderData>
        get() = maskFromPathsTo.map { FileFinderData(pathFind, it.key) }

    open fun isHideIfNotExists(): Boolean = false

    private fun executeFile(file: File) {

        if(!file.exists()) {
            if(isHideIfNotExists() ) return

            throw IOException("file not found ${file.absolutePath}")
        }

        val pair = maskFromPathsTo.entries
            .firstOrNull { Pattern.compile(it.key, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE).isFind(file.name) }
            ?: return

        val setPath = pair.value()

        if(!File(setPath).exists()) {
            File(setPath).mkdirs()
        }

        file.copyTo(File(setPath + "/" + file.name), true)

        if(isMove) {
            file.delete()
        }
    }

    override  fun execute(elem : Elem) : State {

        LoggerFactory.getLogger(FileMoverRegexp::class.java).info("execute Elem ${elem.name}")

        val file = elem.getFile()

        executeFile(file)

        return State.OK
    }

    override fun actionTask() :ActionTask = this
}