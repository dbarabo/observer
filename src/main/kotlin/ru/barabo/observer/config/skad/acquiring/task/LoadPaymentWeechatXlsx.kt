package ru.barabo.observer.config.skad.acquiring.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.acquiring.Acquiring
import ru.barabo.observer.config.skad.acquiring.loader.XlsxWeechatPaymentLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.config.test.TestConfig
import java.io.File
import java.time.Duration

object LoadPaymentWeechatXlsx : FileFinder, FileProcessor {

    private const val PATH = "C:/Temp/2"

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData(PATH,"paymentsacq_daily_\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d\\.xlsx"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка Weechat xlsx-файла"

    override fun config(): ConfigTask = TestConfig

    private val loader = XlsxWeechatPaymentLoader()

    override fun processFile(file: File) {
        loader.loadPayment(file)
    }
}

