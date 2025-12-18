package ru.barabo.observer.config.fns.ens.task

import oracle.jdbc.OracleTypes
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

object Create311p514 : SingleSelector {

    override fun name(): String = "311-П 5.14 Создать-Smev"

    override fun config(): ConfigTask = EnsConfig

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(9, 0),
        LocalTime.of(15, 55), Duration.ofSeconds(1))

    override val select: String =
        """
select r.id, a.code ||  decode(a.closed, od.max_date, ' O'  || a.opened, ' C' || a.closed)
from od.PTKB_361P_REGISTER r, od.account a 
where r.state = 0 and trunc(r.SENDDATE) = TRUNC(SYSDATE) and r.NUMBER_FILE > 0 and a.doc = r.idaccount
and r.IS_FNS = 1
"""

    override fun execute(elem: Elem): State {

        val dateMore3workDay = AfinaQuery.selectValue(SELECT_DATE_OPEN_MORE_3WORK, arrayOf(elem.idElem!!)) as? Date

        val isContinue = isCheckedElemFirstCall(elem, dateMore3workDay)

        if(!isContinue) {
            return State.NONE
        }

        val file = MessageCreator311p514Fns.createMessage(elem.idElem!!)

        val archive = archiveName(elem.idElem, file)

        Archive.addToZip(archive.absolutePath, file)

        return State.OK
    }

    private fun archiveName(idFile: Long?, fileToArchive: File): File {

        val archive = AfinaQuery.execute(query = EXEC_ADD_TO_ARCHIVE_ZIP,
            params = arrayOf(fileToArchive.nameWithoutExtension, idFile),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

        return File("${fileToArchive.parent}/$archive.zip")
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
}

private const val SELECT_DATE_OPEN_MORE_3WORK = "select od.PTKB_FNS_EXPORT_XML.getDateOpenCloseIfMoreWorkDay3( ? ) from dual"

private const val EXEC_ADD_TO_ARCHIVE_ZIP = "{ call od.PTKB_440P.addToArchive311pZip(?, ?, ?) }"