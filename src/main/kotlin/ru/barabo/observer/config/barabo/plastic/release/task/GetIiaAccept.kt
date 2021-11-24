package ru.barabo.observer.config.barabo.plastic.release.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.release.cycle.StateRelease
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount.hCardInToday
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.time.Duration
import java.util.*

/**
 * SENT -> (SENT_OK | SENT_ERROR)
 * SMS_SENT -> (SMS_SENT_ACCESS | SMS_SENT_ERROR)
 */
object GetIiaAccept: FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(GetIiaAccept::class.java)

    override fun name(): String = "Получить Акцепт на файл"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(LoadRestAccount.hCardIn,"IIA_........_......_0226\\.(ACCEPT|ERROR)"))

    fun File.moveFileHCardInToday() :File {

        val hCardInToday = File("${hCardInToday()}/$name")
        copyTo(hCardInToday, true)
        delete()

        return hCardInToday
    }

    override fun processFile(file: File) {

        val rows = AfinaQuery.select(SELECT_PACKET, arrayOf(file.nameWithoutExtension.uppercase(Locale.getDefault())))

        val hCardInToday = file.moveFileHCardInToday()

        if (rows.size != 1) throw IOException("Файл акцепта не соответствует данным в ptkb_plastic_pack файл:${file.name}")

        val idPacket = rows[0][0] as Number

        val state = StateRelease.stateByDbValue((rows[0][1] as Number).toInt())

        val isAccept = file.name.substringAfterLast(".").uppercase(Locale.getDefault()) == "ACCEPT"

        val error = if (!isAccept) {
            hCardInToday.readText(Charset.forName("CP1251"))
        } else ""

        if (state == StateRelease.SENT || state == StateRelease.SMS_SENT) {

            updateAccept(isAccept, idPacket, state, error, hCardInToday)

        } else throw IOException("Файл акцепта не соответствует состоянию пакета в ptkb_plastic_pack.state=${state?.label} файл:${file.name}")
    }

    private const val SELECT_PACKET = "select id, state from od.ptkb_plastic_pack where instr(upper(app_file), ?) > 0"

    private fun updateAccept(isAccept: Boolean, idPacket: Number, state: StateRelease, error: String, file: File) {

        if(isAccept) {

            val nextState = if(state == StateRelease.SENT) StateRelease.SENT_OK else StateRelease.SMS_SENT_ACCESS

            updateState(idPacket, nextState)
        } else {

            val nextState = if(state == StateRelease.SENT) StateRelease.SENT_ERROR else StateRelease.SMS_SENT_ERROR

            updateError(idPacket, nextState, error, file)
        }
    }

    private const val UPDATE_PACKET_ERROR = "update od.ptkb_plastic_pack set updated = sysdate, state = ?, error = ? where id = ?"

    private fun updateError(idPacket: Number, state: StateRelease, error: String, file :File) {

        val settingSession = AfinaQuery.uniqueSession()

        try {
            AfinaQuery.execute(UPDATE_PACKET_ERROR, arrayOf(state.dbValue, error, idPacket), settingSession)

            AfinaQuery.execute(UPDATE_CONTENT_STATE, arrayOf(state.dbValue, idPacket), settingSession)

            AfinaQuery.commitFree(settingSession)

        } catch (e :Exception) {
            logger.error("updateError", e)

            AfinaQuery.rollbackFree(settingSession)

            throw SessionException(e.message?:"")
        }

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_ERROR,
                body = bodyError(file, idPacket, state, error))
    }

    private fun bodyError(file: File, idPacket: Number, state: StateRelease, error: String) =
            "Ошибка в файле Акцепта <${file.absolutePath}>\n" +
            "od.ptkb_plastic_pack.id = $idPacket\n" +
            "Состояние: ${state.label}" +
            "Ошибка:$error"

    private const val SUBJECT_ERROR = "Пластик: Выпуск Ошибка в файле Акцепта"

    private fun updateState(idPacket: Number, state: StateRelease) {

        val settingSession = AfinaQuery.uniqueSession()

        try {
            AfinaQuery.execute(UPDATE_PACKET_STATE, arrayOf(state.dbValue, idPacket), settingSession)

            AfinaQuery.execute(UPDATE_CONTENT_STATE, arrayOf(state.dbValue, idPacket), settingSession)

            AfinaQuery.commitFree(settingSession)

        } catch (e: Exception) {
            logger.error("updateState", e)

            AfinaQuery.rollbackFree(settingSession)

            throw SessionException(e.message ?: "")
        }
    }

    private const val UPDATE_PACKET_STATE = "update od.ptkb_plastic_pack set updated = sysdate, state = ? where id = ?"

    private const val UPDATE_CONTENT_STATE = "update od.ptkb_plast_pack_content set state = ? where PLASTIC_PACK = ?"

}