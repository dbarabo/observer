package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.task.CreateSaveResponse390p
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
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

object SendByPtkPsdCopy : FileMover, FileFinder {
    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData(::x311pJur, "A.*07717.*\\.ARJ"),
                   FileFinderData(::x311pPhisyc, "BN07717.*\\.ARJ"),
                   FileFinderData(CreateSaveResponse390p::sendFolder390p, "AFT_0507717.*\\.ARJ"))


    override val pathsTo: Array<()->String> = arrayOf(::cPtkPostPost, SendByPtkPsdNoXml::dArchiveOutToday)

    fun cPtkPostPost() :String = "C:/PTK_POST/POST"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false, LocalTime.MIN, LocalTime.of(17, 0), Duration.ofHours(3))

    override val isMove: Boolean = false

    override fun config(): ConfigTask = PtkPsd

    override fun name(): String = "Отправка (311-П)"

    private fun x311pJur() :File = File("X:/311-П/Отправка/${todayFolder()}/CRYPTO")

    private fun x311pPhisyc() :File = File("X:/311-П/ФИЗИКИ/Отправка/${todayFolder()}/CRYPTO")

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())
}