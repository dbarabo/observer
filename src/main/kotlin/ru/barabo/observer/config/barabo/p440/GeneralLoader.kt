package ru.barabo.observer.config.barabo.p440

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.task.FileLoader
import ru.barabo.observer.config.barabo.p440.task.ToUncrypto440p
import ru.barabo.observer.config.cbr.ticket.task.p440.folderLoaded440p
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.XmlLoader
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.config.fns.scad.task.getUncryptoFolderSmev
import ru.barabo.observer.store.Elem
import java.io.File
import java.time.Duration
import java.time.LocalTime
import java.util.*

abstract class GeneralLoader <in T> : FileProcessor, FileFinder where T : AbstractFromFns {

    private val logger = LoggerFactory.getLogger(GeneralLoader::class.java)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(ToUncrypto440p::getUncFolder440p, ".*\\.xml"),
                FileFinderData(::getUncryptoFolderSmev, ".*\\.xml") )

    override fun config(): ConfigTask = P440Config

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    protected open fun saveOtherData(data :T, idFromFns :Number, idPayer :Number, sessionSetting: SessionSetting){}

    override fun isContainsTask(task :ActionTask?): Boolean = (task is GeneralLoader<*>)

    override fun createNewElem(file :File) : Elem = Elem(file, actionTask(file.name), accessibleData.executeWait)

    fun actionTask(name :String) : ActionTask {

        val actionTask = try {
            FileLoader.objectByPrefix(name.substring(0, 3).uppercase(Locale.getDefault()))
        } catch (e: Exception) {
            logger.error("actionTask=$name", e)

            throw Exception(e)
        }

        return actionTask ?: throw SessionException("unknown file type for 440p $name")
    }

    override fun processFile(file: File) {

        val data = convertFromXml(file)

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

        file.moveToLoaded()
    }

    private fun convertFromXml(file: File): T {
        return try {
            XmlLoader<T>().load(file)
        } catch (e: Exception) {
            logger.error("XmlLoader.load", e)

            throw XmlLoadException(e.message ?: "")
        }
    }
}

internal fun File.moveToLoaded() {
    val fileLoaded = File("${folderLoaded440p().absolutePath}/${name}")

    copyTo(fileLoaded, true)

    delete()
}

fun ParamsQuery.saveData(idFromFns: Number, sessionSetting: SessionSetting,
                         queryTemplate: (String, String) -> String): Number {

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

internal fun AbstractFromFns.saveData(file :File, sessionSetting: SessionSetting) :Number {

    val idFromFns = AfinaQuery.nextSequence(sessionSetting)

    val param = params

    val source = if(file.parent.indexOf(SMEV_CHECK, ignoreCase = true) >= 0) 1 else 0

    param.add(source)

    param.add(file.name.uppercase(Locale.getDefault()))

    param.add(idFromFns)

    val query = insertFnsFrom(listColumns, questionsFromColumns(listColumns))

    AfinaQuery.execute(query, param.toTypedArray(), sessionSetting)

    return idFromFns
}

private fun insertFnsFrom(columns :String, questions :String) =
        "insert into od.ptkb_440p_fns_from ($columns) values ($questions)"

private fun questionsFromColumns(columns :String) = columns.split(",").joinToString(",") { _ -> "?" }

const val SMEV_CHECK = "smev"