package ru.barabo.observer.config.task.template.db

import ru.barabo.observer.config.task.ActionTask

interface SingleSelector : DbSelector, ActionTask {

    override fun actionTask(selectorValue :Any?) = this

    override fun actionTask() = this
}