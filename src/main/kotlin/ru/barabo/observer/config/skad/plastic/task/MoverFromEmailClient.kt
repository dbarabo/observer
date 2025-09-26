package ru.barabo.observer.config.skad.plastic.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.cbr.task.getCbrRequestToday
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.file.FileMoverRegexp
import java.time.Duration
import java.time.LocalTime

object MoverFromEmailClient : FileMoverRegexp("C:/archive_all",
    mapOf("K301BB.*\\.p7m" to ::xNbkiToday,
        "ZBR_0021_NOCRD.*\\.xml" to { getCbrRequestToday().absolutePath },
        "ZBR_0021_NOCRD.*\\.xml\\.txt" to { getCbrRequestToday().absolutePath }
        ), true
) {

    override fun name(): String = "Перенос файлов из почты"

    override fun config(): ConfigTask = PlasticOutSide

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.ALL_DAYS, true,
        LocalTime.of(8, 0), LocalTime.of(23, 50), Duration.ZERO)
}