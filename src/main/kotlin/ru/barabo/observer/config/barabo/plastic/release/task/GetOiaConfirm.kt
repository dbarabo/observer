package ru.barabo.observer.config.barabo.plastic.release.task

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.release.cycle.StateRelease
import ru.barabo.observer.config.barabo.plastic.release.cycle.TypePacket
import ru.barabo.observer.config.barabo.plastic.release.task.GetIiaAccept.moveFileHCardInToday
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import java.io.File
import java.nio.charset.Charset
import java.time.Duration

/**
 * SENT_OK -> (RESPONSE_OK_ALL | RESPONSE_ERROR_ALL)
 * SMS_SENT_ACCESS -> (SMS_RESPONSE_OK_ALL_OIA | SMS_RESPONSE_ERROR_ALL_OIA)
 */
object GetOiaConfirm: FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(GetOiaConfirm::class.java)

    override fun name(): String = "OIA: Подтверждение Обработки"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(LoadRestAccount.hCardIn,"OIA_........_......_0226"))

    override fun processFile(file: File) {

        val hCardInToday = file.moveFileHCardInToday()

        try {

            processOiaFile(hCardInToday)

        } catch (e :Exception) {
            logger.error("processFile", e)

            throw SessionException(e.message?:"")
        }

        CheckWaitOci.execute(Elem())
    }

    private fun processOiaFile(file: File) {

        var errorList = ""

        val packetList = HashSet<Number>()

        for (line in file.readLines(Charset.forName("CP1251"))) {

            val idApplication =  line.substring(58, 58 + 12).trim()

            val btrtType = line.substring(49, 49 + 9).trim()

            var priorState = priorStateForOia(btrtType)

            val result = line.substring(110).trim()

            val iiaFile = line.substring(18, 18 + 30).trim()

            var content = AfinaQuery.select(SELECT_CONTENT_ID, arrayOf(idApplication, priorState.dbValue))

            if(content.isEmpty() && priorState != StateRelease.SMS_SENT_ACCESS) {

                val newPrior = priorStateForOia("BTRT35")
                val newContent = AfinaQuery.select(SELECT_CONTENT_ID, arrayOf(idApplication, newPrior.dbValue))
                if(newContent.isNotEmpty()) {
                    priorState = newPrior
                    content = newContent
                }
            }

            if(content.isEmpty()) {
                errorList += errorInfo(file, line, idApplication, priorState, result, iiaFile)

                continue
            }

            content.forEach {

                val idContent = it[0] as Number

                val packet = it[1] as Number

                val typePacket = TypePacket.getTypePacketByDbValue((it[2] as Number).toInt())

                packetList += packet

                val nextState = nextState(idContent, priorState, result, typePacket)

                if(nextState == StateRelease.RESPONSE_ERROR_ALL || nextState == StateRelease.SMS_RESPONSE_ERROR_ALL_OIA) {
                    errorList += errorInfo(file, line, idApplication, priorState, result, iiaFile, nextState)
                }
            }
        }

        processPacketList(packetList, file)

        processErrorList(errorList)
    }

    private fun processErrorList(errorList: String) {
        if(errorList.isEmpty()) return

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_OIA, body = errorList)
    }

    private const val SUBJECT_OIA = "Пластик: Ошибки при обработки OIA-файла"

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

    private fun processPacketList(packets: Set<Number>, file: File) {

        packets.forEach { packet ->

            AfinaQuery.execute(UPDATE_PACKET_FILE,
                    arrayOf(file.nameWithoutExtension, packet, file.nameWithoutExtension))

            AfinaQuery.execute(EXEC_NO_SMS_CHECK, arrayOf(packet))

            val states = AfinaQuery.select(SELECT_STATE_PACKET, arrayOf(packet))

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

    private const val UPDATE_PACKET_STATE = "update od.ptkb_plastic_pack set updated = sysdate, state = ? where id = ?"

    private const val SELECT_STATE_PACKET = "select pc.state, count(*) from od.ptkb_plast_pack_content pc " +
            "where pc.plastic_pack = ? group by pc.state"

    private const val EXEC_NO_SMS_CHECK = "{ call od.PTKB_PLASTIC_AUTO.updateStateNoSms(?) }"

    private const val UPDATE_PACKET_FILE = "update od.ptkb_plastic_pack set updated = sysdate, " +
            "LOAD_FILES = LOAD_FILES || ? || ';' where id = ? and instr(nvl(LOAD_FILES, ' '), ?) <= 0"

    private fun nextState(idContent: Number, state: StateRelease, result: String, typePacket: TypePacket?): StateRelease {

        val isOk = "Processed OK".equals(result, true)

        val nextState = if(isOk) {
            if(state == StateRelease.SENT_OK)
                StateRelease.RESPONSE_OK_ALL
            else StateRelease.SMS_RESPONSE_OK_ALL_OIA
        } else {
            if(state == StateRelease.SENT_OK)
                StateRelease.RESPONSE_ERROR_ALL
            else StateRelease.SMS_RESPONSE_ERROR_ALL_OIA
        }

        AfinaQuery.execute(UPDATE_CONTENT_STATE, arrayOf(nextState.dbValue, result, idContent))

        return advancedProcessLockUnLock(nextState, typePacket, isOk, idContent, result)
    }

    private fun advancedProcessLockUnLock(nextState: StateRelease, typePacket: TypePacket?,
                                          isOk: Boolean, idContent: Number, result: String): StateRelease {

        if(typePacket !in listOf(TypePacket.BTRT15_ACTIVE, TypePacket.BTRT15_SUSPEND)) return nextState

        val execProcByStatus = if(isOk) EXEC_SEND_SMS_BLOCK_UNBLOCK else EXEC_CHANGE_STATE_TO_FAIL

        AfinaQuery.execute(execProcByStatus, arrayOf(idContent))

        if(isOk || (typePacket == TypePacket.BTRT15_ACTIVE)) return nextState

        return processResultLock(result)
    }

    private fun processResultLock(result: String): StateRelease {

        return if(isSuccessFailInvalidCard(result) ) {
            StateRelease.RESPONSE_OK_ALL
        } else {
            StateRelease.RESPONSE_ERROR_ALL
        }
    }

    private fun isSuccessFailInvalidCard(result: String): Boolean =
            (result.indexOf("Command STCC01 cannot change hot card status from CHST9 to CHST3;", 0, true) >= 0) ||
            (result.indexOf("new statuses of the card are the same as previous (CRST0, CHST3) - (CRST0, CHST3);", 0, true) >= 0)



    private const val EXEC_CHANGE_STATE_TO_FAIL = "{ call od.PTKB_PLASTIC_AUTO.changeStateCardToForeverFail(?) }"

    private const val EXEC_SEND_SMS_BLOCK_UNBLOCK = "{ call od.PTKB_PLASTIC_AUTO.sendSmsBlockUnblock(?) }"

    private const val UPDATE_CONTENT_STATE = "update od.ptkb_plast_pack_content set state = ?, msg_oia = ? where id = ?"

    private const val SELECT_CONTENT_ID = "select pc.id, pc.plastic_pack, pc.TYPE_PACK from ObjDesc o, od.ptkb_plast_pack_content pc" +
            " where o.DescText = ? and pc.app_card = o.doc and pc.state = ?"

    private fun priorStateForOia(btrtType: String) : StateRelease =
            if(btrtType.equals("BTRT35", true)) StateRelease.SMS_SENT_ACCESS else StateRelease.SENT_OK

}