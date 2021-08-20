package ru.barabo.observer.config.correspond.task

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import org.apache.log4j.Logger
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.correspond.ContainerBase64
import ru.barabo.observer.config.correspond.Correspond
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.p311.ticket.FileRecord
import ru.barabo.observer.config.task.p311.ticket.NameRecords
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.LocalDateTime
import java.util.*
import java.util.regex.Pattern

private fun correspondReceiveFolder() = File("C:/oev/Exg/rcv")

@Transient
private val logger = Logger.getLogger(DecryptEdFile::class.java.name)

object DecryptEdFile : FileFinder, FileProcessor {

    override fun name(): String = "Расшифровать и положить"

    override fun config(): ConfigTask = Correspond

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::correspondReceiveFolder ))

    override val accessibleData: AccessibleData = AccessibleData (workWeek = WeekAccess.ALL_DAYS)

    override fun processFile(file: File) {

        tryProcessSentError(file) {

            copyToArchiveOthersFile(file)

            if(file.isCorrespondToday() ) {
                uncryptMoveFile(file)
            } else {
                file.delete()

                if(file.isNoTodayCorrespond() ) {
                    throw Exception ("isNoTodayCorrespond файл не прошел по маске file=$file")
                }
            }
        }
    }
}

private fun tryProcessSentError(file: File, process: ()->Unit) {
    try {
        process()
    } catch (e: Exception) {

        logger.error("tryProcessSentError file=${file.absolutePath}")

        logger.error("tryProcessSentError", e)

        sentErrorMessage(file,e.message?:"")
    }
}

private fun sentErrorMessage(file: File, message: String) {

    val to = BaraboSmtp.AUTO[0]

    val subject = "Ошибка в задаче DecryptEdFile (Коррсчет получить файл)"

    val body = "Файл = ${file.absolutePath}\n$message"

    AfinaQuery.execute(SEND_ERROR, arrayOf(to, subject, body))
}

private const val SEND_ERROR = "{ call od.PTKB_SENDMAIL.sendSimple( ?, ?, ? ) }"

private fun uncryptMoveFile(file: File) {

    val decodeFile =  loadDecodeFile(file)

    val zFile = File("$Z_IN/${file.name}")

    decodeFile.copyTo(zFile, true)

    file.delete()
}

private fun loadDecodeFile(file: File): File {
    val container = initXStream().fromXML(file) as ContainerBase64

    val infoBase64 = container.objectBase64

    val decode = Base64.getDecoder().decode(infoBase64)

    val decodeFile =  File("${UNSIGN_PATH}/${file.name}")

    decodeFile.writeBytes(decode)

    if(!decodeFile.exists()) {
        throw Exception("Ошибка при декодировании файла $file")
    }

    return decodeFile
}

private fun initXStream() = XStream(DomDriver("CP1251")).apply {
    processAnnotations(ContainerBase64::class.java)
    processAnnotations(NameRecords::class.java)
    processAnnotations(FileRecord::class.java)

    useAttributeFor(String::class.java)
    useAttributeFor(Int::class.java)
}

private fun copyToArchiveOthersFile(file: File): File {

    val fileTo = File("${archivePathToday().absolutePath}/${file.name}")

    file.copyTo(fileTo, true)

    return fileTo
}

private fun archivePathToday() = "${ARCHIVE_PATH}/${Get440pFiles.todayFolder()}".byFolderExists()

private const val ARCHIVE_PATH = "D:/ARCHIVE"

private const val Z_IN = "z:/in"

const val UNSIGN_PATH = "C:/oev/Exg/afina"

private fun File.isCorrespondToday(): Boolean {
    val dayByMoscow = "%02d".format( LocalDateTime.now().minusHours(7).dayOfMonth )

    return listOf(
        "050771.*Packet(EPD|EID|ESID|Cash).${dayByMoscow}.*",
        "050771.*ED....${dayByMoscow}.*",
        "ED...${dayByMoscow}.*"
    ).map {
        Pattern.compile(it, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)
    }.firstOrNull { it.isFind(this.name) } != null
}

private fun File.isNoTodayCorrespond(): Boolean {

    return listOf(
        "050771.*Packet(EPD|EID|ESID|Cash).*",
        "050771.*ED.......*",
        "ED......*"
    ).map {
        Pattern.compile(it, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)
    }.firstOrNull { it.isFind(this.name) } != null
}

