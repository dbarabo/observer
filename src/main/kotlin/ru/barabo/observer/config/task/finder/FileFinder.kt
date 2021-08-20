package ru.barabo.observer.config.task.finder

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.regex.Pattern

interface FileFinder: Executor {
    val fileFinderData :List<FileFinderData>

    override fun findAbstract(): Executor?  = findFiles()

    fun isContainsTask(task :ActionTask?): Boolean = (task == this)

    fun findElemInStore(idElem :Long, name :String): Boolean =
            StoreSimple.existsElem(::isContainsTask, idElem, name, accessibleData.isDuplicateName)

    fun createNewElem(file :File) :Elem = Elem(file, actionTask(), accessibleData.executeWait)

    private fun findFiles() :Executor?  {

        var result: Executor? = null

        val startDayNow = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        try {
            fileFinderData.forEach { ff ->

             ff.directory().listFiles { f ->
                   (!f.isDirectory) &&
                    ( ff.search?.isFind(f.name, ff.isNegative)?:true ) &&
                    ( (!ff.isModifiedTodayOnly) || f.isModifiedMore(startDayNow) ) &&

                    (!findElemInStore(f.lastModified(), f.name))
                }?.forEach {
                    val newElem = createNewElem(it)

                    StoreSimple.save(newElem)

                    result = this
                }
            }
        }catch(e :Exception) {
            LoggerFactory.getLogger(FileFinder::class.java).error("findFiles", e)
            result = null
        }

        return result
    }
}

fun File.isModifiedMore(moreTime :Long) :Boolean = lastModified() >= moreTime

fun Pattern.isFind(name :String, isNegative:Boolean = false): Boolean {
    var isFind :Boolean = this.matcher(name)?.matches()?:false?:false

    if(isNegative) {
        isFind = !isFind
    }

    return isFind
}