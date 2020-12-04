package ru.barabo.observer.config.skad.acquiring.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.config.test.TestConfig
import java.io.File
import java.time.Duration

object ClearintLoader : FileFinder, FileProcessor {

    private val PATH = LoadRestAccount.hCardIn

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(PATH,"CLEARINT_\\d\\d\\d\\d\\d\\d\\d\\d\\_\\d\\d\\d\\d\\d\\d_0226_\\d\\d\\d\\d\\.html"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка CLEARINT html-файла"

    override fun config(): ConfigTask = TestConfig //Acquiring

    override fun processFile(file: File) {
        // TODO LoadPaymentWeechatXlsx.loader.loadPayment(file)

        val moveFile = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()
    }
}
