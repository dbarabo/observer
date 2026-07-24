package ru.barabo.observer.config.portal.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object Send364pArchiveKesdt : FileMover, FileFinder {

    override fun name(): String = "Отправка 364-П (KESDT*.arj)"

    override fun config(): ConfigTask = PortalTransport

    override val fileFinderData: List<FileFinderData> = listOf(
            FileFinderData( ::getFolder364pForFind, Pattern.compile("KESDT_.*\\.arj", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE) )
        )

    override val pathsTo: Array<()->String> = arrayOf(::sendPortalSzFolder, ::send364pCopy)

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val isMove: Boolean = true
}

private fun todayF(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()).byFolderExists().absolutePath

fun send364pCopy(): String = "${getFolder364pForFind().absolutePath}/${todayF()}"

fun getFolder364pForFind() = File("X:/364-П/Отправка")

private fun sendPortalSzFolder() = "Y:/foiv-sz/answer"



