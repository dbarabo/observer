package ru.barabo.observer.config.fns.cbr.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.data.dateFormatInFile
import ru.barabo.observer.config.fns.cbr.CbrConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

object ArchiveCreator : SingleSelector {

    override fun name(): String = "ЦБ-выписки в Архив"

    override fun config(): ConfigTask = CbrConfig

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
        workTimeTo = LocalTime.of(20, 0), executeWait = Duration.ofSeconds(1))

    override val select: String =
        "select id, FILENAME from od.PTKB_CBR_EXT_RESPONSE where state = 1 and ARCHIVE_ID is null order by FILENAME desc"

    override fun execute(elem: Elem): State {

        val session = AfinaQuery.uniqueSession()

        if(elem.name.uppercase().indexOf("PB") == 0) {
            return createNewArchiveForPb(elem, session)
        }

        val isPbAlreadyInArchive = AfinaQuery.selectValue(SELECT_IS_PB_IN_ARCHIVE, arrayOf(elem.idElem)) as Number

        if(isPbAlreadyInArchive.toInt() == 0) {

            elem.executed = LocalDateTime.now().plusSeconds(10)
            return State.NONE
        } else {
            return findOrCreateNewArchiveForExtract(elem, session)
        }
    }

    private fun findOrCreateNewArchiveForExtract(elem: Elem, session: SessionSetting): State {

        try {
            val (archiveName, requestFilename) = findOrCreateNewArchive(elem.name, session)

            val folderPath = getCbrResponseFolderByRequest(requestFilename).absolutePath

            val extFilename = String.format(elem.name, dateFormatInFile())

            val fileExt = File("$folderPath/$extFilename.xml")

            val archiveFile = File("$folderPath/$archiveName.zip")

            Archive.addToZip(archiveFile.absolutePath, fileExt)

            AfinaQuery.commitFree(session)

        } catch (e: Exception) {

            logger.error("createNewArchiveForPb", e)

            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }

        return State.OK
    }


    private fun createNewArchiveForPb(elem: Elem, session: SessionSetting): State {

        try {
            val (archiveName, requestFilename) = createNewArchive(elem.name, session)

            val folderPath = getCbrResponseFolderByRequest(requestFilename).absolutePath

            val fileExt = File("$folderPath/${elem.name}.xml")

            val archiveFile = File("$folderPath/$archiveName.zip")

            Archive.packToZip(archiveFile.absolutePath, fileExt)

            AfinaQuery.commitFree(session)

            sendMailInfoPb(elem.idElem!!, archiveFile, requestFilename)

        } catch (e: Exception) {

            logger.error("createNewArchiveForPb", e)

            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }

        return State.OK
    }

    private fun createNewArchive(pbFilename: String, session: SessionSetting): Pair<String, String> {
        val (archiveName, requestFile) = AfinaQuery.execute(CALL_CREATE_ARCHIVE, arrayOf(pbFilename),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR, OracleTypes.VARCHAR), sessionSetting = session)!!

        return Pair(archiveName as String, requestFile as String)
    }

    private fun findOrCreateNewArchive(extractFilename: String, session: SessionSetting): Pair<String, String> {
        val (archiveName, requestFile) = AfinaQuery.execute(CALL_FIND_CREATE_ARCHIVE, arrayOf(extractFilename),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR, OracleTypes.VARCHAR), sessionSetting = session)!!

        return Pair(archiveName as String, requestFile as String)
    }

    private fun sendMailInfoPb(idResponsePbFile: Long, archiveFile: File, requestFilename: String) {

        val (statusPb, mailSender) = AfinaQuery.selectCursor(SELECT_STATUS_MAIL, arrayOf(idResponsePbFile))[0]

        if(mailSender?.toString()?.isEmpty() != false) return

        val subject = "Подтверждение о получении запроса $requestFilename"

        val body =
            "Файл ${archiveFile.name} содержит подтверждение о получении запроса ЦБ $requestFilename с кодом $statusPb\n" + getNextTextByCode(statusPb.toString())

        BaraboSmtp.sendStubThrows(to = arrayOf(mailSender.toString()), bcc = BaraboSmtp.OPER_YA, subject = subject, body = body,
            attachments = arrayOf(archiveFile)
        )
    }

    private fun getNextTextByCode(statusPb: String): String {
        return if(statusPb == "01") {
            "Ожидайте в течении 30 минут следующий архив с выписками по запросу"
        } else {
            "Так как код обработки операции не равен '01', то других файлов ответов по этому запросу НЕ будет"
        }
    }
}

private val logger = LoggerFactory.getLogger(ArchiveCreator::class.java)

private const val CALL_CREATE_ARCHIVE = "{ call od.PTKB_CBR_EXTRACT.createNewArchive( ?, ?, ? ) }"

private const val CALL_FIND_CREATE_ARCHIVE = "{ call od.PTKB_CBR_EXTRACT.findOrCreateNewArchive( ?, ?, ? ) }"

private const val SELECT_STATUS_MAIL = "{ ? = call od.PTKB_CBR_EXTRACT.getStateMailByPbId( ? ) }"

private const val SELECT_IS_PB_IN_ARCHIVE = "select od.PTKB_CBR_EXTRACT.isPbFileAlreadyInArchive( ? ) from dual"