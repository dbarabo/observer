package ru.barabo.observer.config.fns.cbr.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.cbr.CbrConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object ArchiveCloseer : SingleSelector {
    override fun name(): String = "Закрыть Архив с выписками"

    override fun config(): ConfigTask = CbrConfig

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
        workTimeTo = LocalTime.of(20, 0), executeWait = Duration.ofSeconds(1))

    override val select: String = "{ ? = call od.PTKB_CBR_EXTRACT.getArchiveNotClosed }"

    override fun isCursorSelect(): Boolean = true

    override fun execute(elem: Elem): State {

        val (mail, requestFileNoExt) = AfinaQuery.execute(CALL_CLOSE_ARCHIVE, arrayOf(elem.idElem),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR, OracleTypes.VARCHAR))!!

        if(mail?.toString()?.isEmpty() != false) return State.OK

        val archive = findArchiveByName(elem.name, requestFileNoExt as String)

        sendMailInfo(mail.toString(), archive, requestFileNoExt)

        return State.OK
    }

    private fun findArchiveByName(archiveNameNoExt: String, requestFileNoExt: String): File {
        val folderPath = getCbrResponseFolderByRequest(requestFileNoExt).absolutePath

        return File("$folderPath/${archiveNameNoExt}.zip")
    }

    private fun sendMailInfo(mail: String, archiveFile: File, requestFilename: String) {

        val subject = "Архив с выписками по запросу $requestFilename"

        val body = "Файл ${archiveFile.name} содержит выписки согласно запросу ЦБ $requestFilename"

        BaraboSmtp.sendStubThrows(to = arrayOf(mail), bcc = BaraboSmtp.OPER_YA, subject = subject, body = body,
            attachments = arrayOf(archiveFile)
        )
    }
}

private const val CALL_CLOSE_ARCHIVE = "{ call od.PTKB_CBR_EXTRACT.closeArchive( ?, ?, ? ) }"