package ru.barabo.observer.config.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State

/**
 * Действие элемента
 */
interface ActionTask {

    fun name() :String

    fun config() : ConfigTask

    fun execute(elem : Elem) :State

    fun isSendError() :Boolean = true
}