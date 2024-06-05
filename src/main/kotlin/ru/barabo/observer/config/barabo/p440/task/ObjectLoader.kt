package ru.barabo.observer.config.barabo.p440.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.GeneralLoader
import ru.barabo.observer.config.barabo.p440.XmlLoadException
import ru.barabo.observer.config.barabo.p440.moveToLoaded
import ru.barabo.observer.config.barabo.p440.saveData
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.fz263.load.xml.uno.OrderTaxInfo263fz
import ru.barabo.observer.config.task.fz263.load.xml.uno.UnoFromFns
import ru.barabo.observer.config.task.fz263.load.xml.upo.UpoFromFns
import ru.barabo.observer.config.task.p440.load.ver4.request.ZsoFromFnsVer4
import ru.barabo.observer.config.task.p440.load.ver4.request.ZsvFromFnsVer4
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



object UnoLoader : GeneralLoader<UnoFromFns>() {

    override fun name(): String = "Загрузка UNO-файла (инкассо-263-ФЗ)"

    override fun config(): ConfigTask = EnsConfig

    override fun saveOtherData(data: UnoFromFns, idFromFns: Number, idPayer: Number, sessionSetting: SessionSetting) {

        val unoData = (data.fromFnsInfo as OrderTaxInfo263fz).unoData

        unoData.saveData(idFromFns, sessionSetting, ::insertPno)
    }

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2FileZsv(file, UnoFromFns::emptyFromFns)
        }
    }
}

object UpoLoader : GeneralLoader<UpoFromFns>() {

    override fun name(): String = "Загрузка UPO-файла (арест-263-ФЗ)"

    override fun config(): ConfigTask = EnsConfig

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2FileZsv(file, UpoFromFns::emptyUpoFromFns)
        }
    }
}

object RpoLoader : GeneralLoader<RpoFromFns>() {

    override fun name(): String = "Загрузка RPO-файла (арест)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: XmlLoadException) {
            createPb2FileZsv(file, RpoFromFns::emptyRpoFromFns)
        }
    }
}

object RooLoader : GeneralLoader<RooFromFns>() {

    private val logger = LoggerFactory.getLogger(RooLoader::class.java)

    override fun name(): String = "Загрузка ROO-файла (отмена)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: XmlLoadException) {
            createPb2FileZsv(file, RooFromFns::emptyRooFromFns)
        }
    }
}

object ZsnLoader : GeneralLoader<ZsnFromFns>() {

    override fun name(): String = "Загрузка ZSN-файла (наличие сч.)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2FileZsv(file, ZsnFromFns::emptyZsnFromFns)
        }
    }
}

private val logger = LoggerFactory.getLogger(ZsoLoader::class.java)

object ZsoLoaderVer4 : GeneralLoader<ZsoFromFnsVer4>() {

    override fun name(): String = "Загрузка ZSO-файла (остатки) ver.4"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            //createPb2File(file)
            createPb2FileZsv(file, ZsoFromFnsVer4::emptyZsoFromFns)
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

object ZsvLoaderVer4 : GeneralLoader<ZsvFromFnsVer4>() {

    override fun name(): String = "Загрузка ZSV-файла (выписка) ver.4"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2FileZsv(file, ZsvFromFnsVer4::emptyZsvFromFns)
        }
    }
}

object ZsvLoader : GeneralLoader<ZsvFromFns>() {

    override fun name(): String = "Загрузка ZSV-файла (выписка)"

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            createPb2FileZsv(file)
        }
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

private fun GeneralLoader<*>.createPb2FileZsv(file: File, emptyZsvCreator: ()->AbstractFromFns = ZsvFromFns::emptyZsvFromFns) {
    val elem = StoreSimple.findElemByFile(file.name, file.parent, this.actionTask(file.name))
        ?: throw SessionException("elem file=$file not found")

    elem.error =
        "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB1 с ошибкой расшифрования"
    BaraboSmtp.errorSend(elem)

    val zsvPb2 = emptyZsvCreator()

    val uniqueSession = AfinaQuery.uniqueSession()

    try {

        val idFnsFrom = zsvPb2.saveData(file, uniqueSession)

        updateCheckCodeError(idFnsFrom, uniqueSession)

        AfinaQuery.commitFree(uniqueSession)
    } catch (e: Exception) {

        logger.error("createPb2File", e)

        AfinaQuery.rollbackFree(uniqueSession)

        throw SessionException(e.message ?: "")
    }

    file.moveToLoaded()
}

private fun updateCheckCodeError(idFnsFrom: Number, sessionSetting: SessionSetting) {

    val params = arrayOf<Any?>(UNCRYPTO_ERROR_CODE, UNCRYPTO_ERROR_TEXT, idFnsFrom)

    AfinaQuery.execute(UPDATE_CHECK_CODE, params, sessionSetting)
}

private const val UNCRYPTO_ERROR_CODE = "05"

private const val UNCRYPTO_ERROR_TEXT = "Ошибка расшифрования файла. Ключ шифрования неверен."

private const val UPDATE_CHECK_CODE = "update od.ptkb_440p_fns_from set check_codes = ?, check_text_errors = ? where id = ?"

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



