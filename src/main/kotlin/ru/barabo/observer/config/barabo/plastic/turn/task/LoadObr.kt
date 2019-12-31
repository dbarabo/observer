package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
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
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.nio.charset.Charset
import java.time.Duration

object LoadObr  : FileFinder, FileProcessor, PosLengthLoader {
    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(LoadRestAccount.hCardIn,
            "OBR_.*"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка OBR-файла ответа"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun processFile(file: File) {

        isExistsError = false

        fileProcess = file

        load(file, Charset.forName("CP1251"))

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()

        if(isExistsError) {
            BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = "Ошибки в OBR файле", body = bodyErrorObr(idFile))
        }
    }

    private var isExistsError = false

    private const val SELECT_ERROR_OBR =  "{ ? = call od.PTKB_PLASTIC_TURNOUT.getErrorListByFile( ? ) }"

    private fun headerError(file :File) = "OBR-файл ${file.name} пришел с ошибками\n"

    private fun bodyErrorObr(idFile :Any) :String {
        val dataList = AfinaQuery.selectCursor(SELECT_ERROR_OBR, arrayOf(idFile))

        return  headerError(fileProcess) + dataList.joinToString("\n") { it.joinToString("\t") }
    }

    private lateinit var fileProcess :File

    override val headerColumns: Array<Column> = arrayOf( Column(0, 0) { fileProcess.name } )

    override val bodyColumns: Array<Column> =arrayOf(
            Column(39, 12, LoadObi::parseInt ),
            Column(52, 8, LoadObi::parseToString)
    )

    override val tailColumns: Array<Column> = emptyArray()

    override val headerQuery: String? = "{ call od.PTKB_PLASTIC_TURNOUT.addObrFile(?, ?) }"

    override val bodyQuery: String? = "{ call od.PTKB_PLASTIC_TURNOUT.setErrorObr(?, ?, ?) }"

    override fun generateHeaderSequense(line: String, sessionSetting: SessionSetting): Any? {

        val column = Column(20, 18, LoadObi::parseInt)

        idFile = column.calculate(line)

        val isExists = AfinaQuery.selectValue(SELECT_EXISTS_ID, arrayOf(idFile), sessionSetting)

        isExists?.let { it }?: throw Exception("Исходный файл для ответного ${fileProcess.name} id=$idFile не найден")

        return idFile
    }

    private const val SELECT_EXISTS_ID = "select id from od.PTKB_IBI_MAIN m where m.id = ?"

    override val tailQuery: String? = null

    private lateinit var idFile :Any

    override fun getTypeLine(line: String, order: Int): TypeLine {
        if(line.length < 6) return TypeLine.NOTHING

        return when (line.substring(0, 6).toUpperCase()) {
            "RCTP01"-> TypeLine.HEADER
            "RCTP12"-> {
                isExistsError = true

                TypeLine.BODY
            }
            "RCTP02"-> TypeLine.TAIL

            else-> throw Exception("not found type string $line")
        }
    }
}