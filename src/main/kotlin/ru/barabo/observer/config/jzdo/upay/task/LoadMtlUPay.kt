package ru.barabo.observer.config.jzdo.upay.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.CtlLoader
import ru.barabo.observer.config.jzdo.upay.UPayConfig
import ru.barabo.observer.config.jzdo.upay.task.UncryptoUPay.uncryptoFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration

object LoadMtlUPay : CtlLoader(), FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::uncryptoFolder,
            "ZKMTL.*_0133.*"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка UPay MTL-файла"

    override fun config(): ConfigTask = UPayConfig

    override fun processFile(file: File) {

        loadCtlMtl(file)
    }
}