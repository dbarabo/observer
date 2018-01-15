package ru.barabo.observer.config.barabo.p440

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.task.FileLoader
import ru.barabo.observer.config.barabo.p440.task.ToUncrypto440p
import ru.barabo.observer.config.barabo.p440.task.ToUncrypto440p.getUncFolder440p
import ru.barabo.observer.config.cbr.ticket.task.p440.TicketLoader
import ru.barabo.observer.config.cbr.ticket.task.p440.folderLoaded440p
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.p440.load.XmlLoader
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreDerby
import java.io.File
import java.time.Duration
import java.time.LocalTime

abstract class GeneralLoader <in T> : FileProcessor, FileFinder where T : AbstractFromFns {

    private val logger = LoggerFactory.getLogger(GeneralLoader::class.java)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(ToUncrypto440p::getUncFolder440p, ".*\\.xml"))

    override fun config(): ConfigTask = P440Config

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun processFile(file: File) {

        val data = XmlLoader<T>().load(file)

        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            val idFromFns = data.saveData(file, uniqueSession)

            val idPayer = data.payer.saveData(idFromFns, uniqueSession,  ::insertPayer)

            saveOtherData(data, idFromFns, idPayer, uniqueSession)

        } catch (e: Exception) {

            logger.error("saveData", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        AfinaQuery.commitFree(uniqueSession)

        val fileLoaded = File("${folderLoaded440p()}/${file.name}")

        file.copyTo(fileLoaded, true)
        file.delete()
    }

    open protected fun saveOtherData(data :T, idFromFns :Number, idPayer :Number, sessionSetting: SessionSetting){}

    override fun findAbstract(): Executor? {

        var count = 0

        fileFinderData.forEach { ff ->
            count += ff.directory().listFiles { f ->
                (f != null) && (!f.isDirectory) && ((ff.search == null) || (ff.search.isFind(f.name, ff.isNegative))) }
                    ?.map { fl -> Elem(fl, actionTask(fl.name), accessibleData.executeWait) }
                    ?.filter { el -> (el.task !is TicketLoader<*>) && StoreDerby.addNotExistsByIdName(el, accessibleData.isDuplicateName) }?.count()?:0
        }

        return if(count > 0)this else null
    }

    private fun actionTask(name :String) : ActionTask {

        val actionTask = FileLoader.objectByPrefix(name.substring(0, 3).toUpperCase())

        return actionTask ?: throw SessionException("unknown file type for 440p $name")
    }
}

fun ParamsQuery.saveData(idFromFns :Number, sessionSetting: SessionSetting, queryTemplate :(String, String) -> String) :Number {

    val id = AfinaQuery.nextSequence(sessionSetting)

    val param = params

    param.add(idFromFns)

    param.add(id)

    val query = queryTemplate(listColumns, questionsFromColumns(listColumns))

    AfinaQuery.execute(query, param.toTypedArray(), sessionSetting)

    return id
}

private fun insertPayer(columns :String, questions :String) =
        "insert into od.ptkb_440p_client ($columns) values ($questions)"

private fun AbstractFromFns.saveData(file :File, sessionSetting: SessionSetting) :Number {

    val idFromFns = AfinaQuery.nextSequence(sessionSetting)

    val param = params

    param.add(file.name.toUpperCase())

    param.add(idFromFns)

    val query = insertFnsFrom(listColumns, questionsFromColumns(listColumns))

    AfinaQuery.execute(query, param.toTypedArray(), sessionSetting)

    return idFromFns
}

private fun insertFnsFrom(columns :String, questions :String) =
        "insert into od.ptkb_440p_fns_from ($columns) values ($questions)"

private fun questionsFromColumns(columns :String) = columns.split(",").joinToString(",") { _ -> "?" }