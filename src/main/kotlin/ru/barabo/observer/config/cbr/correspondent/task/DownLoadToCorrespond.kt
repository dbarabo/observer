package ru.barabo.observer.config.cbr.correspondent.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.correspondent.Correspondent
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


object DownLoadToCorrespond : FileMover, FileFinder {
    override val fileFinderData: List<FileFinderData> =
        listOf(FileFinderData("c:/TA/Block_ras/in", ".*\\.(ED|EDS|SF|SFD)"))

    override val pathsTo: Array<() -> String> = arrayOf(::dTaBack, /*::jVepToday,*/ ::xVepInbound)

    private fun dTaBack() :String = "C:/TA/BACK/${todayFolder()}"

    private fun xVepInbound() :String = "X:/VEP/INBOUND"

    //private fun jVepToday() :String = "J:/VEP/CRY_RAS/${todayFolder()}"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(20))

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override val isMove: Boolean = true

    override fun config(): ConfigTask = Correspondent

    override fun name(): String = "Приход"
}