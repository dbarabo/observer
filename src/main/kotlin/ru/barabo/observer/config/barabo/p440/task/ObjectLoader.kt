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
import ru.barabo.observer.config.task.fz263.load.xml.upv.UpvFromFns
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
}

object UpoLoader : GeneralLoader<UpoFromFns>() {

    override fun name(): String = "Загрузка UPO-файла (арест-263-ФЗ)"

    override fun config(): ConfigTask = EnsConfig

    override fun processFile(file: File) {

        try {
            super.processFile(file)
        } catch (e: Exception) {
            logger.error("UpoLoader.processFile e.message=${e.message}")

            if((e.message?.indexOf("ORA-20000: Не указан счет(а) для файла ФНС") ?: -1) >= 0) {
                createPb2FileZsv(file, UpoFromFns::emptyUpoFromFns)
            } else {
                throw Exception(e)
            }
        }
    }
}

object UpvLoader : GeneralLoader<UpvFromFns>() {

    override fun name(): String = "Загрузка UPV-файла (продажа валют)"

    override fun config(): ConfigTask = EnsConfig
}

object RpoLoader : GeneralLoader<RpoFromFns>() {

    override fun name(): String = "Загрузка RPO-файла (арест)"
}

object RooLoader : GeneralLoader<RooFromFns>() {

    private val logger = LoggerFactory.getLogger(RooLoader::class.java)

    override fun name(): String = "Загрузка ROO-файла (отмена)"
}

object ZsnLoader : GeneralLoader<ZsnFromFns>() {

    override fun name(): String = "Загрузка ZSN-файла (наличие сч.)"
}

private val logger = LoggerFactory.getLogger(ZsoLoaderVer4::class.java)

object ZsoLoaderVer4 : GeneralLoader<ZsoFromFnsVer4>() {

    override fun name(): String = "Загрузка ZSO-файла (остатки) ver.4"
}

object ZsvLoaderVer4 : GeneralLoader<ZsvFromFnsVer4>() {

    override fun name(): String = "Загрузка ZSV-файла (выписка) ver.4"

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

private const val UNCRYPTO_ERROR_CODE = "15"

private const val UNCRYPTO_ERROR_TEXT = "Не задан реквизит Счет или ЭСП Одновременно оба этих элемента не могут отсутствовать"

private const val UPDATE_CHECK_CODE = "update od.ptkb_440p_fns_from set check_codes = ?, check_text_errors = ? where id = ?"

private fun getErrorTypeFormat(file: File): String? {

    val text = try {
        file.readText(Charset.forName("windows-1251"))
    } catch (e: Exception) {

        return null
    }

    val startIndex = text.indexOf("ТипИнф=")

    val endIndex = text.indexOf("ВерсПрог=")

    if(startIndex !in 0..<endIndex) {
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



