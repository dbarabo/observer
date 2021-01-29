package ru.barabo.observer.config.skad.plastic.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object MoveTicketNbki : FileMover, FileFinder {

    override fun name(): String = "НБКИ перенос квитка"

    override fun config(): ConfigTask = PlasticOutSide

    override val fileFinderData: List<FileFinderData> =
        listOf(FileFinderData("D:/archive_all", "K301BB.*\\.p7m"))

    override val pathsTo: Array<() -> String> = arrayOf(::xNbkiToday)

    fun xNbkiToday(): String = "X:/НБКИ/${todayFolder()}"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true,
        LocalTime.of(9, 0), LocalTime.of(23, 50), Duration.ZERO)

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override val isMove: Boolean = true

}