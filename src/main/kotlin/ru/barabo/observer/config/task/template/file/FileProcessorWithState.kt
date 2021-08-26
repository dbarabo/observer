package ru.barabo.observer.config.task.template.file

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.io.IOException

interface FileProcessorWithState  : ActionTask, Executor {

    fun isThrowWhenNotExists(): Boolean = true

    fun processFile(file: File, elem: Elem): State

    override fun execute(elem: Elem): State {

        LoggerFactory.getLogger(FileProcessorWithState::class.java).info("execute Elem ${elem.name}")

        val file = elem.getFile()

        if (!file.exists()) {
            if(isThrowWhenNotExists() ) {
                throw IOException("file not found ${file.absolutePath}")
            } else {
                return State.ARCHIVE
            }
        }

        return processFile(file, elem)
    }

    override fun actionTask() = this
}
