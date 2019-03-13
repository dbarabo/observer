package ru.barabo.observer.config.cbr.ptkpsd.task

import oracle.jdbc.OracleTypes
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.clobToString
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.task.iiaFile
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo.hCardOutSentTodayByFolder
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.config.test.TestConfig
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.sql.Clob
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object SendBlockUnblockAccountPC : SinglePerpetual {

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 2

    override fun config(): ConfigTask = TestConfig//OtherCbr

    override fun name(): String = "(Раз\\)Блокировка карт.счетов в ПЦ"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(9, 0), workTimeTo = LocalTime.of(19, 0) )

    override fun execute(elem: Elem): State {

        if(isBlockTargetInverse(elem)) {
            blockCardAccounts()
        } else {
            unBlockCardAccounts()
        }

        return State.NONE
    }

    private fun isBlockTargetInverse(elem: Elem): Boolean {
        val isBlock = elem.target != TARGET_UNBLOCK

        elem.target = if(isBlock) TARGET_UNBLOCK else TARGET_BLOCK

        return isBlock
    }

    private fun blockCardAccounts(): Boolean {

        val session = AfinaQuery.uniqueSession()

        try {
            val idPacket = addAccountsToBlockList(session)  ?: return commitFree(session)

            if(!createSendFile(idPacket, session)) return false

        } catch (e: Exception) {
            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }

        return commitFree(session)
    }

    private fun unBlockCardAccounts(): Boolean {

        val session = AfinaQuery.uniqueSession()

        try {
            val idPacket = addAccountsToUnBlocks(session)  ?: return commitFree(session)

            if(!createSendFile(idPacket, session)) return false

        } catch (e: Exception) {
            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }

        return commitFree(session)
    }

    private fun createSendFile(idPacket: Long, session: SessionSetting): Boolean {
        val iiaFile = iiaFile()

        val clob = AfinaQuery.execute(EXEC_CREATE_FILE_BLOCK_UNBLOCK, arrayOf(idPacket, iiaFile), session,
                intArrayOf(OracleTypes.CLOB))!![0] as? Clob ?: return commitFree(session)

        val file = File("${hCardOutSentTodayByFolder()}/$iiaFile")

        if(file.exists()) throw Exception("Файл уже существует $file")

        file.writeText(clob.clobToString(), charset = Charset.forName("cp1251"))

        val toJzdo = File("${IbiSendToJzdo.toJzdoSent()}/${file.name}")
        file.copyTo(toJzdo, true)

        return true
    }

    private fun commitFree(session: SessionSetting): Boolean {
        AfinaQuery.commitFree(session)

        return false
    }

    private fun addAccountsToUnBlocks(session: SessionSetting): Long? {
        val accounts = AfinaQuery.selectCursor(SELECT_UNBLOCK)

        var priorPacket: Long? = null

        for(accountParams in accounts) {
            val packetId = AfinaQuery.execute(EXEC_ADD_UNBLOCK, accountParams, session, intArrayOf(OracleTypes.NUMBER))!![0] as? Number
                    ?: throw SessionException("Не удалось добавить счет в пакет на РАЗблокировку accountId=${accountParams[0]}")

            if(packetId.toLong() != (priorPacket?.let { it } ?: packetId.toLong() ) ) {
                throw SessionException("При добавлении счетов пакет разблок, пакет обновился: packetId=$packetId priorPacket = $priorPacket")
            }

            priorPacket = packetId.toLong()
        }

        return priorPacket
    }

    private fun addAccountsToBlockList(session: SessionSetting): Long? {
        val accounts = AfinaQuery.selectCursor(SELECT_BLOCK_ACCOUNTS)

        var priorPacket: Long? = null

        for(accountParams in accounts) {
            val packetId = AfinaQuery.execute(EXEC_ADD_BLOCK_ACCOUNT, accountParams, session, intArrayOf(OracleTypes.NUMBER))!![0] as? Number
                    ?: throw SessionException("Не удалось добавить счет в пакет на блокировку accountId=${accountParams[0]}")

            if(packetId.toLong() != (priorPacket?.let { it } ?: packetId.toLong() ) ) {
                throw SessionException("При добавлении счетов пакет, пакет обновился: packetId=$packetId priorPacket = $priorPacket")
            }

            priorPacket = packetId.toLong()
        }

        return priorPacket
    }

    private const val TARGET_BLOCK = "BLOCK"

    private const val TARGET_UNBLOCK = "UNBLOCK"

    private const val SELECT_BLOCK_ACCOUNTS: String = "{ ? = call od.PTKB_440P.getCardAccountsMustBeBlock }"

    private const val EXEC_ADD_BLOCK_ACCOUNT: String = "{ call od.PTKB_PLASTIC_AUTO.addToSuspendAccount(?, ?) }"

    private const val SELECT_UNBLOCK: String = "{ ? = call od.PTKB_440P.getCardAccountsMustBeUnBlock }"

    private const val EXEC_ADD_UNBLOCK: String = "{ call od.PTKB_PLASTIC_AUTO.addToUnblockAccount(?, ?) }"

    private const val EXEC_CREATE_FILE_BLOCK_UNBLOCK: String = "{ call od.PTKB_PLASTIC_AUTO.createFileBlockUnBlockAccount(?, ?, ?) }"
}