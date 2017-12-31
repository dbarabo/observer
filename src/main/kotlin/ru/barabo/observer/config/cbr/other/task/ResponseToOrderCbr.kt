package ru.barabo.observer.config.cbr.other.task

import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.rtf.RtfWriter2
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.CbrSmtp
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


object ResponseToOrderCbr : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(::dGuprim, "OD.*\\.pdf"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = OtherCbr

    override fun name(): String = "Ответ на ЦБ-приказ"

    private fun dGuprim() = File("D:/Arhiv_in/GUPRIM/${todayFolder()}")

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    private fun nowFolder() :String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HHmmss") )

          //  DateTimeFormatter.ofPattern("yyyy/MM/dd/HHmmss").format(LocalDateTime.now())

    private fun dArchiveOutNow() = File("D:/ARHIV_OU/${nowFolder()}")


    override fun processFile(file : File) {

        val directoryTo = dArchiveOutNow()
        if(!directoryTo.exists()) {
            directoryTo.mkdirs()
        }

        val sentFilePath = "${directoryTo.absolutePath}/Uvedomlenie.rtf"

        val text = textResponse(file)

        val sentFile = createRtf(sentFilePath, text)

        CbrSmtp.sendCbInfo(sentFile)
    }

    private fun nowTimeMinute() :String = DateTimeFormatter.ofPattern("HH:mm").format(LocalTime.now())

    private val SELECT_PRIOR_WORK_DAY = "select od.getpriorworkday(sysdate - 1, 1000131227) from dual"

    private fun textResponse(file :File) :String {
        val numberOrder = file.name.replace("\\D+".toRegex(), "")

        val priorWorkDay = SimpleDateFormat("dd.MM.yyyy").format(AfinaQuery.selectValue(SELECT_PRIOR_WORK_DAY))

        return "ООО 'Примтеркомбанк' уведомляет о принятии и прочтении приказа за номером: ОД-$numberOrder от " +
                "$priorWorkDay в ${nowTimeMinute()} ${todayFolder()}г."
    }

    private fun createRtf(fileName :String, text :String) :File {

        val document = Document()

        RtfWriter2.getInstance(document, FileOutputStream(fileName))

        document.open()

        document.add(Paragraph(text))

        document.close()

        return File(fileName)
    }
}