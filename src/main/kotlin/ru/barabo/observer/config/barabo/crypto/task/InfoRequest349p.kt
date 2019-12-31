package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object InfoRequest349p : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::folder349p,".*\\.ZIP"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
            LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = ScadConfig

    override fun name(): String = "349-П Scad Расшифровать-уведомить"

    private fun folder349p() :File = File("X:/349-П/${todayFolder()}")

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun processFile(file: File) {

        ScadComplex.decodeAny(file)

        Archive.extractFromZip(file, folder349p().absolutePath)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, bcc = BaraboSmtp.AUTO,
                subject = SUBJECT_349P, body = body349p())
    }

    private const val SUBJECT_349P = "Пришел запрос от ФСФМ по 349-П"

    private fun body349p() = "Пришел запрос от ФСФМ по 349-П. Файлы находятся по адресу:${folder349p()}"
}