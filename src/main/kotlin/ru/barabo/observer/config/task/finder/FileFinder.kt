package ru.barabo.observer.config.task.finder

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreDerby
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDate
import java.time.ZoneId
import java.util.regex.Pattern

interface FileFinder :Executor {
    val fileFinderData :List<FileFinderData>

    override fun findAbstract(): Executor?  = findFiles()

    private fun findFiles() :Executor?  {

        var count = 0

        try {
            fileFinderData.forEach { ff ->

                count += ff.directory().listFiles { f ->
                    (f != null) && (!f.isDirectory) && ((ff.search == null) ||
                            ff.search.isFind(f.name, ff.isNegative) ) && ((!ff.isModifiedTodayOnly) || f.isModifiedToday() ) }
                        ?.map { fl -> Elem(fl, actionTask(), accessibleData.executeWait) }
                        ?.filter { el -> StoreDerby.addNotExistsByIdName(el, accessibleData.isDuplicateName) }?.count()?:0
            }
        }catch(e :Exception) {
            LoggerFactory.getLogger(FileFinder::class.java).error("findFiles", e)
            count = 0
        }

        return if(count > 0) this else null
    }
}

private fun File.isModifiedToday() :Boolean {
    return try {
        Files.readAttributes<BasicFileAttributes>(this.toPath(), BasicFileAttributes::class.java)
                .lastModifiedTime().toMillis() >= LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch ( e : IOException) {
        true
    }
}

fun Pattern.isFind(name :String, isNegative :Boolean): Boolean {
    var isFind :Boolean = this.matcher(name)?.matches()?:false?:false
    //LoggerFactory.getLogger(FileFinder::class.java).info("isFind isFind=$isFind name=$name isNegative=$isNegative")

    if(isNegative) {
        isFind = !isFind
    }

    return isFind
}