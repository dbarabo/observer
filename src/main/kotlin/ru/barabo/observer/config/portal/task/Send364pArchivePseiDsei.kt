package ru.barabo.observer.config.portal.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.portal.PortalTransport
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileMover
import java.time.Duration
import java.time.LocalTime
import java.util.regex.Pattern

object Send364pArchivePseiDsei : FileMover, FileFinder {

    override fun name(): String = "Отправка 364-П (PSEI/DSEI*.arj)"

    override fun config(): ConfigTask = PortalTransport

    override val fileFinderData: List<FileFinderData> = listOf(
        FileFinderData( ::getFolder364pForFind, Pattern.compile("[PD][SC][EK][IR]_.*\\.arj", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE) )
    )

    override val pathsTo: Array<()->String> = arrayOf(::sendPortalTzFolder, ::send364pCopy)

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val isMove: Boolean = true
}

private fun sendPortalTzFolder() = "Y:/foiv-tz/answer"
