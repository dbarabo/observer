package ru.barabo.observer.config.barabo.plastic.turn.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.QuoteSeparatorLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime

object BinLoader : FileFinder, FileProcessor, QuoteSeparatorLoader {

    override fun name(): String = "Загрузка файла BIN-ов"

    override fun config(): ConfigTask = PlasticTurnConfig

    override val fileFinderData: List<FileFinderData> = listOf(
        FileFinderData(hCardBin,"BIN_TABLE_UNIVERSAL_\\d\\d\\d\\d\\d\\d\\d\\d\\.csv")
    )

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY,
        workTimeFrom = LocalTime.of(8, 0), workTimeTo = LocalTime.of(20, 0),
        executeWait = Duration.ofSeconds(1))

    private lateinit var fileProcess: File

    override fun processFile(file: File) {

        fileProcess = file

        load(file, Charset.forName("CP1251"))

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        if(!file.delete()) {
            logger.error("FILE IS NOT DELETED $file")
        }
    }

    override val headerQuery: String = "insert into od.ptkb_bin_file (id, FILE_NAME) values (?, ?)"

    override val headerColumns: Map<Int, (String?) -> Any> = mapOf(
        -1 to { _: String? -> fileProcess.name}
    )

    override val bodyQuery: String = "{ call od.PTKB_PLASTIC_TURN.saveBinRecord(?, ?, ?, ?, ?, ?, ?, ?, ?) }"

    override val bodyColumns: Map<Int, (String?) -> Any> = mapOf(

        0 to ::parseToString,
        1 to ::parseToString,
        2 to ::parseInt,
        3 to ::parseInt,
        4 to ::parseInt,
        6 to ::parseToString,
        7 to ::parseToString,
        8 to ::parseToString
    )

    override val tailColumns: Map<Int, (String?) -> Any> = emptyMap()

    override val tailQuery: String? = null

    override fun getTypeLine(fields: List<String>, order: Int): TypeLine {
        if(fields.isEmpty() ) return TypeLine.NOTHING

        return if(order == 0) TypeLine.HEADER else TypeLine.BODY
    }
}

val hCardBin = "H:/КартСтандарт/BIN".ifTest("C:/КартСтандарт/BIN")

private val logger = LoggerFactory.getLogger(BinLoader::class.java)!!