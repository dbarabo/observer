package ru.barabo.observer.config.barabo.p440.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.GeneralLoader
import ru.barabo.observer.config.barabo.p440.XmlLoadException
import ru.barabo.observer.config.barabo.p440.moveToLoaded
import ru.barabo.observer.config.barabo.p440.saveData
import ru.barabo.observer.config.task.p440.load.xml.pno.OrderTax
import ru.barabo.observer.config.task.p440.load.xml.pno.PnoFromFns
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File

object PnoLoader : GeneralLoader<PnoFromFns>() {

    private val logger = LoggerFactory.getLogger(PnoLoader::class.java)

    override fun name(): String = "Загрузка PNO-файла (инкасс)"

    override fun saveOtherData(data: PnoFromFns, idFromFns: Number, idPayer: Number, sessionSetting: SessionSetting) {

        val pnoData =  (data.fromFnsInfo as OrderTax).pnoData

        pnoData?.saveData(idFromFns, sessionSetting, ::insertPno)
    }

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

        val pnoPb2 = PnoFromFns.EmptyFromFns()

        val uniqueSession = AfinaQuery.uniqueSession()

        try {
            pnoPb2.saveData(file, uniqueSession)

            AfinaQuery.commitFree(uniqueSession)
        } catch (e: Exception) {

            logger.error("createPb2File", e)

            AfinaQuery.rollbackFree(uniqueSession)

            throw SessionException(e.message ?: "")
        }

        file.moveToLoaded()
    }
}

private fun insertPno(columns :String, questions :String) =
        "insert into od.PTKB_440P_PNO ($columns) values ($questions)"