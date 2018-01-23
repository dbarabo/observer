package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.Column
import ru.barabo.observer.config.barabo.plastic.turn.loader.PosLengthLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.nio.charset.Charset
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalTime

object LoadRestAccount : FileFinder, FileProcessor, PosLengthLoader {

    val hCardIn = "H:/КартСтандарт/in"

    fun hCardInToday() = "$hCardIn/${Get440pFiles.todayFolder()}"

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(hCardIn, "ACC_\\d{8}_\\d{6}_0226"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(6, 0))

    override fun name(): String = "Загрузка остатков по счетам"

    override fun config(): ConfigTask  = PlasticTurnConfig

    private lateinit var fileProcess :File

    override fun processFile(file: File) {

        fileProcess = file

        this.load(file, Charset.forName("CP1251"))

        val moveFile = File("${hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()
    }

    private fun restFromString(rest :String?) :Double {

        return (rest?.trim()?.toLong()?.toDouble()?:0.0) / 100
    }

    override val bodyColumns: Array<Column> = arrayOf(
            Column(0, 32) { it?.trim()?:String::class.javaObjectType},
            Column(32, 17) {(it?.trim()?.toLong()?.toDouble()?:0.0) / 100}
    )

    override val tailColumns: Array<Column> = emptyArray()

    override val headerColumns: Array<Column> = arrayOf(
            Column(0, 0) { fileProcess.name },
            Column(0, 17) { Timestamp(SimpleDateFormat("yyyyMMdd_HH:mm:ss").parse(it).time) }
    )

    override val headerQuery: String? = "insert into od.ptkb_plastic_acc (id, FILE_NAME, REST_DATE) values (?, ?, ?)"

    override val bodyQuery: String? = "call od.PTKB_PLASTIC_TURN.addAccountRest(?, ?, ?)"

    override val tailQuery: String? = null

    override fun getTypeLine(line: String, order: Int): TypeLine = if(order == 0) TypeLine.HEADER else TypeLine.BODY
}