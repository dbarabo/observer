package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.config.barabo.plastic.release.task.autoupdate.AutoUpdatePlasticJar
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import java.time.LocalTime

object AutoUpdatePlasticJarSoft : AutoUpdatePlasticJar() {

    override fun params(): Array<Any?>? = arrayOf(PROGRAM_NAME, IS_CRITICAL_FALSE)

    override fun name(): String = "AutoUpdate Plastic.jar Теневой"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(15, 0), workTimeTo = LocalTime.of(9, 0))
}