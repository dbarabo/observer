package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.config.barabo.plastic.release.task.autoupdate.AutoUpdatePlasticJar
import ru.barabo.observer.config.task.AccessibleData
import java.time.LocalTime

object AutoUpdatePlasticJarCritical : AutoUpdatePlasticJar() {

    override fun name(): String = "AutoUpdate Plastic.jar Критический"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
            workTimeTo = LocalTime.of(20, 0))

    override fun params(): Array<Any?>? = arrayOf(PROGRAM_NAME, IS_CRITICAL_TRUE)
}