package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.cmd.Cmd
import ru.barabo.cmd.deleteFolder
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ptkpsd.task.f101.DbSaver101Form
import ru.barabo.observer.config.cbr.ticket.task.TicketSimple
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime

object Load101FormXml : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(::xReceiptTodayFolder, "Ф101_40507717\\.xml"))

    private fun xReceiptTodayFolder() = TicketSimple.xReceiptToday().byFolderExists()

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    override fun name(): String = "Загрузка 101 Формы XML"

    override fun config(): ConfigTask = PtkPsd

    override fun processFile(file: File) {

        val tempFolder = Cmd.tempFolder("f101")

        val xml101 = File("${tempFolder.absoluteFile}/${file.name}")

        val textXml = file.readText(Charset.forName("CP1251"))

        xml101.writeText(textXml)

        DbSaver101Form.loadSave(xml101)

        tempFolder.deleteFolder()
    }
}