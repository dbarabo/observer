package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.Column
import ru.barabo.observer.config.barabo.plastic.turn.loader.PosLengthLoader
import ru.barabo.observer.config.barabo.plastic.turn.loader.TypeLine
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.util.*

object LoadRateTT057 : PosLengthLoader, FileFinder, FileProcessor {

    override fun name(): String = "Загрузка Курсов Mastercard TT057"

    override fun config(): ConfigTask  = PlasticTurnConfig

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(LoadRestAccount.hCardIn,
    "TT057..\\.\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\.\\d\\d\\d"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun processFile(file: File) {
        load(file, Charset.forName("CP1251"))

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()
    }

    private var dateRate: Any? = null

    override fun getTypeLine(line: String, order: Int): TypeLine {
        if (line.length < 2) return TypeLine.NOTHING

        return when (line.substring(0, 1).uppercase(Locale.getDefault())) {
            "H" -> {
                dateRate = parseDateTime(line.substring(1, 15).trim())
                TypeLine.NOTHING
            }
            "D" -> if (line.substring(1, 4) == "643") TypeLine.BODY else TypeLine.NOTHING

            "T" -> TypeLine.NOTHING

            else -> throw Exception("not found type string $line")
        }
    }

    override val bodyQuery: String = "{ call od.PTKB_PLASTIC_TURN.addExchangeRate(?, ?, ?, ?, ?) }"

    override val bodyColumns: Array<Column> = arrayOf(
            Column(0, 0) { dateRate!! },
            Column(4, 3, LoadObi::parseInt),
            Column(10, 15, ::parseRate),
            Column(25, 15, ::parseRate),
            Column(40, 15, ::parseRate)
    )

    override val headerColumns: Array<Column> = emptyArray()

    override val tailColumns: Array<Column> = emptyArray()

    override val headerQuery: String? = null

    override val tailQuery: String? = null

    private fun parseRate(rate: String?): Any = rate!!.toLong() / 10000000.0
}