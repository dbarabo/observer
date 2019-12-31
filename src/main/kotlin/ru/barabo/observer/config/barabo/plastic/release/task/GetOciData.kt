package ru.barabo.observer.config.barabo.plastic.release.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.release.cycle.StateRelease
import ru.barabo.observer.config.barabo.plastic.release.task.GetIiaAccept.moveFileHCardInToday
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.time.Duration

/**
 * RESPONSE_OK_ALL -> (OCI_ALL | ?)
 */
object GetOciData: FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(GetOciData::class.java)

    override fun name(): String = "OCI: Загрузка карты"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(LoadRestAccount.hCardIn,"OCI_........_......_0226"))

    override fun processFile(file: File) {

        val hCardInToday = file.moveFileHCardInToday()

        val settingSession = AfinaQuery.uniqueSession()

        try {

            processOciFile(hCardInToday, settingSession)

        } catch (e :Exception) {
            logger.error("processFile", e)

            AfinaQuery.rollbackFree(settingSession)

            throw SessionException(e.message?:"")
        }

        AfinaQuery.commitFree(settingSession)
    }

    private fun processOciFile(file: File, settingSession: SessionSetting) {

        file.readLines(Charset.forName("CP1251")).drop(1).forEach { line ->

            val idApplication =  line.substring(337, 337 + 12).trim()

            val content = AfinaQuery.select(SELECT_CONTENT_ID, arrayOf(idApplication), settingSession)

            val idContent = checkContent(file, idApplication, line, content, settingSession)

            idContent?.apply { processOciLine(this, line, file.name, settingSession) }
        }
    }

    fun processOciLine(idContent: Any, line: String, fileName: String, settingSession: SessionSetting) {
        AfinaQuery.execute(EXECUTE_OCI, arrayOf(idContent, line, fileName), settingSession)
    }

    private fun checkContent(file: File, idApplication: String, line: String, content: List<Array<Any?>>, settingSession: SessionSetting): Number? {

        if(content.isEmpty()) {
            return sendError("Не найден контент для idApplication=$idApplication в файле ${file.name}")
        }

        val idContent = content[0][0] as Number

        val state = StateRelease.stateByDbValue((content[0][1] as Number).toInt())?:StateRelease.MAX


        if(state == StateRelease.RESPONSE_OK_ALL) return idContent

        return if(state.dbValue < StateRelease.RESPONSE_OK_ALL.dbValue) {
            saveWaitOciState(file, idContent, line, settingSession)
        } else {
            sendError("Неверное состояние для ptkb_plast_pack_content.id=" +
                    "$idContent в файле ${file.name} state:${state.label}")
        }
    }

    private fun saveWaitOciState(file: File, idContent: Number, line: String, settingSession: SessionSetting) :Number? {

        AfinaQuery.execute(EXECUTE_WAIT_OCI, arrayOf(idContent, line, file.name), settingSession)

//        BaraboSmtp.sendStubThrows(to = BaraboSmtp.YA, subject = SUBJECT_OCI,
//                body = "Состояние ptkb_plast_pack_content.id=$idContent еще не изменилось, а OCI-файл уже есть file=${file.name}")

        return null
    }

    private const val EXECUTE_WAIT_OCI = "{ call od.PTKB_PLASTIC_AUTO.addWaitOciRecord(?, ?, ?) }"

    private fun sendError(body: String): Number? {

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_OCI, body = body)

        return null
    }

    private const val SUBJECT_OCI = "Пластик: Ошибки при обработки OCI-файла"

    private const val EXECUTE_OCI = "{ call od.PTKB_PLASTIC_AUTO.processOCI(?, ?, ?) }"

    private val SELECT_CONTENT_ID = "select pc.id, pc.state from ObjDesc o, od.ptkb_plast_pack_content pc " +
            "where o.DescText = ? and pc.app_card = o.doc " +
            "and pc.state in (${StateRelease.RESPONSE_OK_ALL.dbValue}, ${StateRelease.SENT_OK.dbValue}) " +
            "order by pc.state desc"
}