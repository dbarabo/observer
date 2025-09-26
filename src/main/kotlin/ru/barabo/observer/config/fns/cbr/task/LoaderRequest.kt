package ru.barabo.observer.config.fns.cbr.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.fns.cbr.CbrConfig
import ru.barabo.observer.config.skad.anywork.task.cbr.extract.loader.CbrRequestLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.time.Duration
import java.time.LocalTime

object LoaderRequest : FileProcessor, FileFinder {

    override fun name(): String = "ЦБ Загрузка запроса выписки"

    override fun config(): ConfigTask = CbrConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY,
        false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> =
        listOf(FileFinderData(::getCbrRequestToday, "ZBR.*\\.xml") )

    override fun processFile(file: File) {

        CbrRequestLoader.loadRequest(file)
    }
}

private val CBR_FOLDER = if (TaskMapper.isAfinaBase()) "X:/ЦБ-ВЫПИСКИ" else "C:/ЦБ-ВЫПИСКИ"

fun getCbrRequestToday(): File = "$CBR_FOLDER/${Get440pFiles.todayFolder()}/Получено".byFolderExists()

fun getCbrResponseToday(): File = "$CBR_FOLDER/${Get440pFiles.todayFolder()}/Отправка".byFolderExists()

fun getCbrResponseFolderByRequest(filenameRequest: String): File =
    "${getCbrResponseToday().absolutePath}/${filenameRequest.substringBefore('.')}".byFolderExists()
