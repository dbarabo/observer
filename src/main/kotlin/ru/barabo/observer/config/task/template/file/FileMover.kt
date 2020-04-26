package ru.barabo.observer.config.task.template.file

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.io.IOException

interface FileMover : ActionTask, Executor {

    val pathsTo: Array<()->String>

    val isMove: Boolean

    fun isHideIfNotExists(): Boolean = false

    fun executeFile(file :File) {

        if(!file.exists()) {
            if(isHideIfNotExists() ) return

            throw IOException("file not found ${file.absolutePath}")
        }

        pathsTo.forEach {
            if(!File(it()).exists()) File(it()).mkdirs()

            file.copyTo(File(it() + "/" + file.name), true)
        }

        if(isMove) {
            file.delete()
        }
    }

    override  fun execute(elem : Elem) : State {

        LoggerFactory.getLogger(FileMover::class.java).info("execute Elem ${elem.name}")

        val file = elem.getFile()

        executeFile(file)

        return State.OK
    }

    override fun actionTask() :ActionTask = this
}