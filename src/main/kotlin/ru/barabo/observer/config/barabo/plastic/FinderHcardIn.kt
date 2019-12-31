package ru.barabo.observer.config.barabo.plastic

import ru.barabo.observer.config.barabo.plastic.release.task.MoveHcardIn
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.store.Elem
import java.io.File
import java.time.Duration

object FinderHcardIn: FileFinder {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData({ File(LoadRestAccount.hCardIn) }))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS)

    override fun isContainsTask(task: ActionTask?): Boolean = task?.let { HcardInObject.isInclude(it) }?: false

    override fun createNewElem(file: File): Elem {

        val objectTask = HcardInObject.objectByFileName(file.name, MoveHcardIn)

        val executeWait = if(objectTask is Executor)objectTask.accessibleData.executeWait else Duration.ofSeconds(1)

        return Elem(file, objectTask, executeWait)
    }

    override fun actionTask(): ActionTask = MoveHcardIn
}