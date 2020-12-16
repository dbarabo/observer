package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.loader.loadClearIntCp1251
import ru.barabo.observer.config.barabo.plastic.turn.loader.saveClearIntToDB
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration

object ClearintLoader : FileFinder, FileProcessor {

    private val PATH = LoadRestAccount.hCardIn

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(PATH,"CLEARINT_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\d_0226_\\d\\d\\d\\d\\.html"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка CLEARINT html-файла"

    override fun config(): ConfigTask = PlasticTurnConfig //Acquiring

    override fun processFile(file: File) {

        val clearIntInfo = loadClearIntCp1251(file)

        saveClearIntToDB(clearIntInfo, file)

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")
        file.copyTo(moveFile, true)
        file.delete()
    }
}
