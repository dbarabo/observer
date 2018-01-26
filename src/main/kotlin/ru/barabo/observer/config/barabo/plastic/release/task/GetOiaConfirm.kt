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

object GetOiaConfirm: FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(GetOiaConfirm::class.java)

    override fun name(): String = "OIA: Подтверждение Обработки"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(LoadRestAccount.hCardIn,"OIA_........_......_0226"))

    override fun processFile(file: File) {

        val hCardInToday = file.moveFileHCardInToday()

        val settingSession = AfinaQuery.uniqueSession()

        try {

            processOiaFile(hCardInToday, settingSession)

        } catch (e :Exception) {
            logger.error("processFile", e)

            AfinaQuery.rollbackFree(settingSession)

            throw SessionException(e.message?:"")
        }

        AfinaQuery.commitFree(settingSession)
    }

    private fun processOiaFile(file :File, settingSession: SessionSetting) {

        var errorList = ""

        val packetList = HashSet<Number>()

        file.readLines().forEach {

            val idApplication =  it.substring(58, 58 + 12).trim()

            val priorState = priorStateForOia(it.substring(49, 49 + 9).trim())

            val result = it.substring(110).trim()

            val iiaFile = it.substring(18, 18 + 30).trim()

            val content = AfinaQuery.select(SELECT_CONTENT_ID, arrayOf(idApplication, priorState.dbValue))

            if(content.size != 1) {
                errorList += errorInfo(file, it, idApplication, priorState, result, iiaFile)

                return@forEach
            }

            val idContent = content[0][0] as Number

            packetList += content[0][1] as Number

            val nextState = nextState(idContent, priorState, result, settingSession)

            if(nextState == StateRelease.RESPONSE_ERROR_ALL || nextState == StateRelease.SMS_RESPONSE_ERROR_ALL_OIA) {
                errorList += errorInfo(file, it, idApplication, priorState, result, iiaFile, nextState)
            }
        }

        processPacketList(packetList, file, settingSession)

        processErrorList(errorList)
    }

    private fun processErrorList(errorList: String) {
        if(errorList.isEmpty()) return

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_OIA, body = errorList)
    }

    private val SUBJECT_OIA = "Пластик: Ошибки при обработки OIA-файла"

    private fun errorInfo(file: File, line: String, idApplication: String, state: StateRelease, result: String,
                          iiaFile: String, nextState: StateRelease? = null) :String {

        return if(nextState == null) {
            "Не найдена строка контента\n"
        } else {"Ошибка в строке контента\n"} +

            "в файле $file\n" +
            "строка:$line\n" +
            "idApp:$idApplication\n" +
            "Состояние:${state.label}\n" +
            "Результат:$result\n" +
            "iiaFile:$iiaFile\n" +
            "стейт перехода:$nextState\n"
    }

    private fun processPacketList(packets: Set<Number>, file: File, settingSession: SessionSetting) {

        packets.forEach { packet ->

            AfinaQuery.execute(UPDATE_PACKET_FILE,
                    arrayOf(file.nameWithoutExtension, packet, file.nameWithoutExtension), settingSession)

            AfinaQuery.execute(EXEC_NO_SMS_CHECK, arrayOf(packet), settingSession)

            val states = AfinaQuery.select(SELECT_STATE_PACKET, arrayOf(packet), settingSession)

            val sumState = processStates(states)

            sumState?.let { AfinaQuery.execute(UPDATE_PACKET_STATE, arrayOf(it.dbValue, packet))  }
        }
    }

    private fun processStates(states :List<Array<Any?>>) :StateRelease? {

        var errorState = StateRelease.CARD_TO_CLIENT

        var okState = StateRelease.CARD_TO_CLIENT

        states.forEach {
            val state = StateRelease.stateByDbValue((it[0] as Number).toInt())?:StateRelease.CARD_TO_CLIENT

            if(state.isErrorState() ) {

                errorState = if(state.dbValue < errorState.dbValue) state else errorState

            } else {

                okState = if(state.dbValue < okState.dbValue) state else okState
            }
        }

        errorState = if(errorState == StateRelease.CARD_TO_CLIENT) okState else errorState

        return if(errorState == StateRelease.CARD_TO_CLIENT) null else errorState
    }

    private val UPDATE_PACKET_STATE = "update od.ptkb_plastic_pack set updated = sysdate, state = ? where id = ?"

    private val SELECT_STATE_PACKET = "select pc.state, count(*) from od.ptkb_plast_pack_content pc " +
            "where pc.plastic_pack = ? group by pc.state"

    private val EXEC_NO_SMS_CHECK = "{ call od.PTKB_PLASTIC_AUTO.updateStateNoSms(?) }"

    private val UPDATE_PACKET_FILE = "update od.ptkb_plastic_pack set updated = sysdate, " +
            "LOAD_FILES = LOAD_FILES || ? || ';' where id = ? and instr(nvl(LOAD_FILES, ' '), ?) <= 0"

    private fun nextState(idContent: Number, state: StateRelease, result: String, settingSession: SessionSetting): StateRelease {

        val isOk = "Processed OK".equals(result, true)

        val nextState = if(isOk) {
            if(state == StateRelease.SENT_OK)
                StateRelease.RESPONSE_OK_ALL else StateRelease.SMS_RESPONSE_OK_ALL_OIA
        } else {
            if(state == StateRelease.SENT_OK)
                StateRelease.RESPONSE_ERROR_ALL else StateRelease.SMS_RESPONSE_ERROR_ALL_OIA
        }

        AfinaQuery.execute(UPDATE_CONTENT_STATE, arrayOf(nextState.dbValue, result, idContent), settingSession)

        return nextState
    }

    private val UPDATE_CONTENT_STATE = "update od.ptkb_plast_pack_content set state = ?, msg_oia = ? where id = ?"

    private val SELECT_CONTENT_ID = "select pc.id, pc.plastic_pack from ObjDesc o, od.ptkb_plast_pack_content pc" +
            " where o.DescText = ? and pc.app_card = o.doc and pc.state = ?"

    private fun priorStateForOia(btrtType: String) : StateRelease =
            if(btrtType.equals("BTRT35", true)) StateRelease.SMS_SENT_ACCESS else StateRelease.SENT_OK

}