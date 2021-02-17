package ru.barabo.observer.config.barabo.p440.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.GeneralLoader
import ru.barabo.observer.config.barabo.p440.XmlLoadException
import ru.barabo.observer.config.barabo.p440.moveToLoaded
import ru.barabo.observer.config.barabo.p440.saveData
import ru.barabo.observer.config.task.p440.load.ver4.request.ZsoFromFnsVer4
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns
import ru.barabo.observer.config.task.p440.load.xml.apx.ApnFromFns
import ru.barabo.observer.config.task.p440.load.xml.apx.ApoFromFns
import ru.barabo.observer.config.task.p440.load.xml.apx.ApzFromFns
import ru.barabo.observer.config.task.p440.load.xml.decision.RooFromFns
import ru.barabo.observer.config.task.p440.load.xml.decision.RpoFromFns
import ru.barabo.observer.config.task.p440.load.xml.request.ZsnFromFns
import ru.barabo.observer.config.task.p440.load.xml.request.ZsoFromFns
import ru.barabo.observer.config.task.p440.load.xml.request.ZsvFromFns
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.nio.charset.Charset

object RpoLoader : GeneralLoader<RpoFromFns>() {

    private val logger = LoggerFactory.getLogger(RpoLoader::class.java)

    override fun name(): String = "Загрузка RPO-файла (арест)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: XmlLoadException) {
            createPb2File(file)
        }
    }

    private fun createPb2File(file: File) {

        val elem = StoreSimple.findElemByFile(file.name, file.parent, actionTask(file.name)) ?: throw SessionException("elem file=$file not found")

        elem.error = "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB2 с ошибкой расшифрования"
        BaraboSmtp.errorSend(elem)

        val rpoPb2 = RpoFromFns.emptyRpoFromFns()

        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            rpoPb2.saveData(file, uniqueSession)

            AfinaQuery.commitFree(uniqueSession)
        } catch (e: Exception) {

            logger.error("createPb2File", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        file.moveToLoaded()
    }
}

object RooLoader : GeneralLoader<RooFromFns>() {

    private val logger = LoggerFactory.getLogger(RooLoader::class.java)

    override fun name(): String = "Загрузка ROO-файла (отмена)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: XmlLoadException) {
            createPb2File(file)
        }
    }

    private fun createPb2File(file: File) {

        val elem = StoreSimple.findElemByFile(file.name, file.parent, actionTask(file.name)) ?: throw SessionException("elem file=$file not found")

        elem.error = "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB2 с ошибкой расшифрования"
        BaraboSmtp.errorSend(elem)

        val rooPb2 = RooFromFns.emptyRooFromFns()

        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            rooPb2.saveData(file, uniqueSession)

            AfinaQuery.commitFree(uniqueSession)
        } catch (e: Exception) {

            logger.error("createPb2File", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        file.moveToLoaded()
    }
}

object ZsnLoader : GeneralLoader<ZsnFromFns>() {

    override fun name(): String = "Загрузка ZSN-файла (наличие сч.)"
}

private val logger = LoggerFactory.getLogger(ZsoLoader::class.java)

object ZsoLoaderVer4 : GeneralLoader<ZsoFromFnsVer4>() {

    override fun name(): String = "Загрузка ZSO-файла (остатки)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2File(file)
        }
    }

    private fun createPb2File(file: File) {

        val type = getErrorTypeFormat(file)

        type?.let { createErrorTypeFormat(file, it) } ?: createErrorUncrypto(file)
    }

    private fun createErrorTypeFormat(file: File, typeFormat: String) {

        val zsoTypeFormat = ZsoFromFnsVer4.emptyZsoFromFnsErrorTypeFormat(typeFormat)

        createError(zsoTypeFormat, file)
    }

    private fun createErrorUncrypto(file: File) {
        val elem = StoreSimple.findElemByFile(file.name, file.parent, ZsoLoaderVer4.actionTask(file.name)) ?: throw SessionException("elem file=$file not found")

        elem.error = "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB2 с ошибкой расшифрования"
        BaraboSmtp.errorSend(elem)

        val zsoPb2 = ZsoFromFnsVer4.emptyZsoFromFns()

        createError(zsoPb2, file)
    }
}

object ZsoLoader : GeneralLoader<ZsoFromFns>() {

    override fun name(): String = "Загрузка ZSO-файла (остатки)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2File(file)
        }
    }

    private fun createPb2File(file: File) {

        val type = getErrorTypeFormat(file)

        type?.let { createErrorTypeFormat(file, it) } ?: createErrorUncrypto(file)
    }

    private fun createErrorTypeFormat(file: File, typeFormat: String) {

        val zsoTypeFormat = ZsoFromFns.emptyZsoFromFnsErrorTypeFormat(typeFormat)

        createError(zsoTypeFormat, file)
    }

    private fun createErrorUncrypto(file: File) {
        val elem = StoreSimple.findElemByFile(file.name, file.parent, actionTask(file.name)) ?: throw SessionException("elem file=$file not found")

        elem.error = "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB2 с ошибкой расшифрования"
        BaraboSmtp.errorSend(elem)

        val zsoPb2 = ZsoFromFns.emptyZsoFromFns()

        createError(zsoPb2, file)
    }
}

object ZsvLoader : GeneralLoader<ZsvFromFns>() {

    private val logger = LoggerFactory.getLogger(ZsvLoader::class.java)

    override fun name(): String = "Загрузка ZSV-файла (выписка)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2File(file)
        }
    }

    private fun createPb2File(file: File) {
        val elem = StoreSimple.findElemByFile(file.name, file.parent, actionTask(file.name)) ?: throw SessionException("elem file=$file not found")

        elem.error = "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB2 с ошибкой расшифрования"
        BaraboSmtp.errorSend(elem)

        val zsvPb2 = ZsvFromFns.emptyZsvFromFns()

        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            zsvPb2.saveData(file, uniqueSession)

            AfinaQuery.commitFree(uniqueSession)
        } catch (e: Exception) {

            logger.error("createPb2File", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        file.moveToLoaded()
    }
}

object ApnLoader : GeneralLoader<ApnFromFns>() {

    override fun name(): String = "Загрузка APN-файла (приост. инкасс)"
}

object ApoLoader : GeneralLoader<ApoFromFns>() {

    override fun name(): String = "Загрузка APO-файла (отм. приост. инкасс)"
}

object ApzLoader : GeneralLoader<ApzFromFns>() {

    private val logger = LoggerFactory.getLogger(ApzLoader::class.java)

    override fun name(): String = "Загрузка APZ-файла (отзыв. инкасс)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: XmlLoadException) {
            createPb2File(file)
        }
    }

    private fun createPb2File(file: File) {

        val elem = StoreSimple.findElemByFile(file.name, file.parent, actionTask(file.name)) ?: throw SessionException("elem file=$file not found")

        elem.error = "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB2 с ошибкой расшифрования"
        BaraboSmtp.errorSend(elem)

        val apzPb2 = ApzFromFns.emptyApzFromFns()

        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            apzPb2.saveData(file, uniqueSession)

            AfinaQuery.commitFree(uniqueSession)
        } catch (e: Exception) {

            logger.error("createPb2File", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        file.moveToLoaded()
    }
}

private fun getErrorTypeFormat(file: File): String? {

    val text = try {
        file.readText(Charset.forName("windows-1251"))
    } catch (e: Exception) {

        return null
    }

    val startIndex = text.indexOf("ТипИнф=")

    val endIndex = text.indexOf("ВерсПрог=")

    if(startIndex < 0 || endIndex <= startIndex) {
        return null
    }

    val subText =  text.substring(startIndex + 7, endIndex)

    val quoteStart =  subText.indexOf('"')

    val quoteEnd =  subText.lastIndexOf('"')

    if(quoteStart < 0 || quoteEnd <= quoteStart) {
        return null
    }

    return subText.substring(quoteStart + 1, quoteEnd)
}

private fun createError(zsoError: AbstractFromFns, file: File) {

    val uniqueSession = AfinaQuery.uniqueSession()

    try {
        zsoError.saveData(file, uniqueSession)

        AfinaQuery.commitFree(uniqueSession)
    } catch (e: Exception) {

        logger.error("createPb2File", e)

        AfinaQuery.rollbackFree(uniqueSession)

        throw SessionException(e.message ?: "")
    }

    file.moveToLoaded()
}



