package ru.barabo.observer.config.barabo.crypto.task

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
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.*

object CreateSaveResponse390p : FileFinder, FileProcessor {

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(Get390pArchive::getFolder390p,"(AFT|TPO|TOO).*\\.(ARJ|VRB)") )

    override fun name(): String = "390-П Ответ на запрос/архив"

    override fun config(): ConfigTask = CryptoConfig

    private fun sendFolder390p() : File = "${Get390pArchive.X390P}/${Get390pArchive.todayFolder()}/Отправлено".byFolderExists()

    override fun processFile(file: File) {

        val responseData = ResponseData(fileName = file.nameWithoutExtension)

        val responseText = generateResponseXml(responseData)

        val responseFile = getResponseFile(file.nameWithoutExtension)

        responseFile.writeText(responseText, Charset.forName("CP1251"))

        Verba.signByBarabo(responseFile)
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
                                val dateCheck: LocalDateTime = LocalDateTime.now(),
                                val codeCheck: String = "01", val descriptionCheck: String = "файл принят") {

    fun dateTimeXml(): String = ISO_LOCAL_DATE_TIME.format(dateCheck)
}