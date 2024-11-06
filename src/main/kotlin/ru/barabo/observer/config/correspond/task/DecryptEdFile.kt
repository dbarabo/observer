package ru.barabo.observer.config.correspond.task

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import com.thoughtworks.xstream.security.AnyTypePermission
import org.apache.log4j.Logger
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.correspond.ContainerBase64
import ru.barabo.observer.config.correspond.ContainerEnvEnvelope
import ru.barabo.observer.config.correspond.Correspond
import ru.barabo.observer.config.correspond.EnvBody
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.p311.ticket.FileRecord
import ru.barabo.observer.config.task.p311.ticket.NameRecords
import ru.barabo.observer.config.task.template.file.FileProcessorWithState
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.regex.Pattern

private fun correspondReceiveFolder() = File("C:/oev/Exg/rcv")

@Transient
private val logger = Logger.getLogger(DecryptEdFile::class.java.name)

object DecryptEdFile : FileFinder, FileProcessorWithState {

    override fun name(): String = "Расшифровать и положить"

    override fun config(): ConfigTask = Correspond

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::correspondReceiveFolder ))

    override val accessibleData: AccessibleData = AccessibleData (workWeek = WeekAccess.ALL_DAYS)

    override fun processFile(file: File, elem: Elem): State {

        return tryProcessSentError(file) {

            copyToArchiveOthersFile(file)

            when {
                // remove save to archive folder
                // isStopTime() -> processFileStopTime(file)

                file.isCorrespondToday() -> uncryptMoveFile(file)

                file.isNoTodayCorrespond() -> uncryptMoveNotLoadFile(file)

                else -> {
                    file.delete()
                    State.OK
                }
            }
        }
    }
}

private fun isStopTime(): Boolean = LocalTime.now().isBefore(LocalTime.of(9, 0))

private fun tryProcessSentError(file: File, process: ()->State): State {
    return try {
        process()
    } catch (e: Exception) {

        logger.error("tryProcessSentError file=${file.absolutePath}")

        logger.error("tryProcessSentError", e)

        sentErrorMessage(file,e.message?:"")

        State.ERROR
    }
}

private fun sentErrorMessage(file: File, message: String) {

    val to = BaraboSmtp.AUTO[0]

    val subject = "Ошибка в задаче DecryptEdFile (Коррсчет получить файл)"

    val body = "Файл = ${file.absolutePath}\n$message"

    AfinaQuery.execute(SEND_ERROR, arrayOf(to, subject, body))
}

private const val SEND_ERROR = "{ call od.PTKB_SENDMAIL.sendSimple( ?, ?, ? ) }"

private fun processFileStopTime(file: File): State {
    if(file.isCorrespondToday()) {

        val decodeFile =  loadDecodeFile(file)

        val dayByMoscow = "%02d".format( LocalDateTime.now().minusHours(7).dayOfMonth )

        val folder = "$Z_IN/$dayByMoscow".byFolderExists().absolutePath

        val zFile = File("$folder/${file.name}")

        decodeFile.copyTo(zFile, true)

        file.delete()
    } else {
        if(file.isNoTodayCorrespond()) {
            uncryptMoveNotLoadFile(file)
        } else {
            file.delete()
        }
    }
    return State.OK
}


private fun uncryptMoveNotLoadFile(file: File): State {
    val decodeFile =  loadDecodeFile(file)

    val findDay = findDayFile(file.name)

    val fileNotLoad = File("${archivePathNotLoadToday(findDay).absolutePath}/${file.name}")

    decodeFile.copyTo(fileNotLoad, true)

    file.delete()

    return State.OK
}

private fun uncryptMoveFile(file: File): State {

    val decodeFile =  loadDecodeFile(file)

    val zFile = File("$Z_IN/${file.name}")

    decodeFile.copyTo(zFile, true)

    file.delete()

    return State.OK
}

private fun containerFromFile(file: File): ContainerBase64 {

    return try {
        initXStream().fromXML(file) as ContainerBase64
    } catch (e: Exception) {

        logger.error("ContainerBase64 file=${file.absolutePath}")
        logger.error("ContainerBase64", e)

        val env = initXStream().fromXML(file) as ContainerEnvEnvelope

        env.envBody.containerBase64
    }
}

fun loadDecodeFile(file: File): File {

    val container = containerFromFile(file)

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
    addPermission(AnyTypePermission.ANY)

    processAnnotations(ContainerBase64::class.java)

    processAnnotations(ContainerEnvEnvelope::class.java)
    processAnnotations(EnvBody::class.java)


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

private fun archivePathNotLoadToday(day: String) = "$Z_IN/$day".byFolderExists()

private const val ARCHIVE_PATH = "D:/ARCHIVE"

private val Z_IN = "z:/in".ifTest("c:/in")

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

private fun findDayFile(fileName: String): String {
    val packetIndex = fileName.indexOf("Packet", ignoreCase = true)
    if(packetIndex > 0) {
       return fileName.find2Digits(packetIndex+6)
    }

    val ed = fileName.indexOf("ED", ignoreCase = true)
    return if(ed == 0 && fileName.length > 6) {
        fileName.substring(5..6)
    } else if(ed > 0 && (fileName.length > ed + 7)) {
        fileName.substring(ed + 6..7 + ed)
    } else {
        "null"
    }
}

private fun String.find2Digits(indexFrom: Int): String {

    val newString = this.substring(indexFrom)

    var beforeFirst: Char? = null
    var first: Char? = null

    for (x in newString) {
        if(x.isDigit()) {
            when {
                (beforeFirst == null) -> beforeFirst = x

                first == null -> first = x

                else -> return "$first$x"
            }
         }
    }

    return "null"
}

