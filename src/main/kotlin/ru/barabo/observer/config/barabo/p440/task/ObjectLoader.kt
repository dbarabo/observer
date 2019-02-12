package ru.barabo.observer.config.barabo.p440.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.GeneralLoader
import ru.barabo.observer.config.barabo.p440.XmlLoadException
import ru.barabo.observer.config.barabo.p440.moveToLoaded
import ru.barabo.observer.config.barabo.p440.saveData
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

object RpoLoader : GeneralLoader<RpoFromFns>() {

    override fun name(): String = "Загрузка RPO-файла (арест)"
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

object ZsoLoader : GeneralLoader<ZsoFromFns>() {

    override fun name(): String = "Загрузка ZSO-файла (остатки)"
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

    override fun name(): String = "Загрузка APZ-файла (отзыв. инкасс)"
}



