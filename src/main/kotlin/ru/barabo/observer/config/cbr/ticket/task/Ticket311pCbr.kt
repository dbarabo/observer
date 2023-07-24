package ru.barabo.observer.config.cbr.ticket.task

import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Ticket311pCbr : FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(Ticket311pCbr::class.java)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","2z...005\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "(311-П ЦБ)"

    fun ticket311p() :String = "X:/311-П/ФИЗИКИ/KVIT/${todayFolder()}"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun processFile(file : File) {
        //Archive.extractFromCab(file, ticket311p(), ".*\\.xml")

        val files = Archive.extractFromCab(file, ticket311p(), ".*\\.xml")?: return

        for(signFile in files) {
            try {
                ScadComplex.unsignAndMoveSource(signFile)
            } catch (e: Exception) {
                logger.error("unsignAndMoveSource", e)
            }
        }
    }
}
