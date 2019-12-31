package ru.barabo.observer.config.jzdo.upay.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.ObiLoad
import ru.barabo.observer.config.jzdo.upay.UPayConfig
import ru.barabo.observer.config.jzdo.upay.task.UncryptoUPay.uncryptoFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import java.io.File
import java.time.Duration

object LoadObiUPay : FileFinder, ObiLoad() {
    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::uncryptoFolder,
            "ZKOBI_\\d\\d\\d\\d\\d\\d\\d\\d_\\d\\d\\d\\d\\d\\d_0133_FEE"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, executeWait = Duration.ofSeconds(1))

    override fun name(): String = "Загрузка OBI_FEE UPay-файла"

    override fun config(): ConfigTask = UPayConfig

    override lateinit var fileProcess: File
}