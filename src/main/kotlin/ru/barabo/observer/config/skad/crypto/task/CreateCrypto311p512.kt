package ru.barabo.observer.config.skad.crypto.task

import oracle.jdbc.OracleTypes
import ru.barabo.archive.Archive
import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.skad.crypto.p311.MessageCreator311p
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

object CreateCrypto311p512 : SingleSelector {
    override fun name(): String = "311-П 5.12 Создать+шифровать"

    override fun config(): ConfigTask = CryptoScad // ScadConfig

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(9, 0),
        LocalTime.of(16, 55), Duration.ofSeconds(1))

    override val select: String =
"""
select r.id, a.code ||  decode(a.closed, od.max_date, ' O'  || a.opened, ' C' || a.closed)
from od.PTKB_361P_REGISTER r, od.account a 
where r.state = 0 and trunc(r.SENDDATE) = TRUNC(SYSDATE) and r.NUMBER_FILE > 0 and a.doc = r.idaccount
"""

    override fun execute(elem: Elem): State {

        val dateMore3workDay = AfinaQuery.selectValue(SELECT_DATE_OPEN_MORE_3WORK, arrayOf(elem.idElem!!)) as? Date

        val isContinue = isCheckedElemFirstCall(elem, dateMore3workDay)

        if(!isContinue) {
            return State.NONE
        }

        val file = MessageCreator311p.createMessage(elem.idElem!!)

        val cryptoFolder = Cmd.createFolder("${file.parent}/CRYPTO")

        val cryptoFile = File("${cryptoFolder.absolutePath}/${file.name}")

        ScadComplex.fullEncode311p(file, cryptoFile, isAddFss = file.name.indexOf("SBC") == 0)

        val archive = archiveName(elem.idElem, cryptoFile)

        Archive.addToArj(archive.absolutePath, arrayOf(cryptoFile) )

        return State.OK
    }

    fun archiveName(idFile: Long?, fileToArchive: File): File {

        val archive = AfinaQuery.execute(query = EXEC_ADD_TO_ARCHIVE,
            params = arrayOf(fileToArchive.nameWithoutExtension, idFile),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

        return File("${fileToArchive.parent}/$archive.ARJ")
    }
}

private fun isCheckedElemFirstCall(elem: Elem, dateMore3workDay: Date?): Boolean {

    if(dateMore3workDay != null && elem.path.isEmpty()) {
        elem.path = "$dateMore3workDay"

        sendMailInfo(elem.name, dateMore3workDay)

        elem.executed = LocalDateTime.now().plusHours(4)

        return false
    }

    return true
}

private fun sendMailInfo(accountCode: String, dateMore3workDay: Date) {
    BaraboSmtp.send(
        to = BaraboSmtp.MANAGERS_UOD,
        bcc = BaraboSmtp.AUTO,
        subject = "311-П Дата открытия/закрытия отправляемого счета старше, чем 3 раб. дня",
        body = "Внимание!\nВ ФНС отправляется счет, дата открытия/закрытия этого счета старше, чем 3 рабочих дня.\n" +
                "№ Счета $accountCode дата открытия/закрытия $dateMore3workDay",
    )
}

private const val EXEC_ADD_TO_ARCHIVE = "{ call od.PTKB_440P.addToArchive311p(?, ?, ?) }"

private const val SELECT_DATE_OPEN_MORE_3WORK = "select od.PTKB_FNS_EXPORT_XML.getDateOpenCloseIfMoreWorkDay3( ? ) from dual"