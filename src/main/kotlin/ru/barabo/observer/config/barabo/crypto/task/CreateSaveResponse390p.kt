package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get390pArchive
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.Verba
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.*

object CreateSaveResponse390p : FileFinder, FileProcessor {

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.of(11, 30),
            LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(Get390pArchive::getFolder390p,"(AFT|TPO|TOO).*\\.(ARJ|VRB)") )

    override fun name(): String = "390-П Ответ на запрос/архив"

    override fun config(): ConfigTask = CryptoConfig

    internal fun sendFolder390p() : File = "${Get390pArchive.X390P}/${Get390pArchive.todayFolder()}/Отправлено".byFolderExists()

    override fun processFile(file: File) {

        generateResponse(file)

        if(file.isCrypto()) {
            file.unCrypto()

            sendMessageInfo(file)
        }
    }

    private fun sendMessageInfo(inputFile: File) {

        val uncryptoFile = "${Get390pArchive.getFolder390pUncrypt()}/${inputFile.nameWithoutExtension}.xml"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.CHECKER_390P, bcc = BaraboSmtp.OPER,
                subject = "Пришли файлы решений по 390-П",
                body = "Файл доступен по адресу:\n$uncryptoFile")
    }

    private fun generateResponse(inputFile: File) {
        val responseData = ResponseData(fileName = inputFile.nameWithoutExtension)

        val responseText = generateResponseXml(responseData)

        val responseFile = getResponseFile(inputFile.nameWithoutExtension)

        responseFile.writeText(responseText, Charset.forName("CP1251"))

        Verba.signByBarabo(responseFile)

        responseFile.addToArchiveSend()
    }

    private fun File.addToArchiveSend() {

        val archive = File("${sendFolder390p().absolutePath}/${getArchiveName()}")

        Archive.addToArj(archive.absolutePath, arrayOf(this) )
    }

    private fun getArchiveName() = "AFT_0507717_FTS0000_${todayArchiveMask()}_001.ARJ"

    private fun todayArchiveMask() :String = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now())

    private fun File.isCrypto(): Boolean = name.indexOf("T") == 0

    private fun File.unCrypto() {
        UnCryptoPacket.addFileToPacket(this, Get390pArchive.getFolder390pUncrypt(), false, "xml")
    }

    private fun getResponseFile(fileNameWithoutExt: String): File =
            File("${sendFolder390p()}/PT1_$fileNameWithoutExt.xml")

    private fun generateResponseXml(responseData: ResponseData) =
"""<?xml version="1.0" encoding="windows-1251"?>
<Подтверждение xmlns="urn:cbr-390P:PT:v1.01"
ИдПодтв="${responseData.guid}" ИмяФайла="${responseData.fileName}"
ДатаВремяПроверки="${responseData.dateTimeXml()}">
	<РезПроверки КодРезПроверки="${responseData.codeCheck}" Пояснение="${responseData.descriptionCheck}"/>
</Подтверждение>"""

}

private data class ResponseData(val guid: String = UUID.randomUUID().toString(), val fileName: String,
                                val dateCheck: LocalDateTime = LocalDateTime.now().withNano(0),
                                val codeCheck: String = "01", val descriptionCheck: String = "файл принят") {

    fun dateTimeXml(): String = ISO_LOCAL_DATE_TIME.format(dateCheck)
}