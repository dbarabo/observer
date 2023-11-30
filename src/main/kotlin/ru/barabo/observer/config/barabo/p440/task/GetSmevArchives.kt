package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.SMEV_CHECK
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.X440P
import ru.barabo.observer.config.cbr.ticket.task.getFolder440p
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalTime

object GetSmevArchives : FileFinder, FileProcessor {

    override fun name(): String = "ЕНС - Получить архивы"

    override fun config(): ConfigTask = EnsConfig //P440Config

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
        LocalTime.of(2, 0), LocalTime.of(23, 30), Duration.ofSeconds(10))

    override val fileFinderData: List<FileFinderData> =
        listOf(FileFinderData(SMEV_IN,"AFN_MIFNS00_0507717_\\d{8}_\\d{5}\\.zip", isModifiedTodayOnly = false))

    override fun processFile(file: File) {

        val newArchiveFile = File("${smevInToday().absolutePath}/${file.name}")

        file.copyTo(newArchiveFile, overwrite = true)
        file.delete()

        val arjArchives = Archive.extractFromZip(newArchiveFile, smevInToday().absolutePath)

        arjArchives?.forEach {
            Archive.extractFromArj(it, smevInToday().absolutePath)
        }

        if(file.exists()) {

            BaraboSmtp.send(to=BaraboSmtp.AUTO, subject="${name()} - Не удалось удалить файл",
                body = "${name()} - Не удалось удалить файл $file")
        }
    }
}

fun smevInToday() = "${getFolder440p().absolutePath}/$SMEV_CHECK".byFolderExists()

val SMEV_IN: String = "$X440P/$SMEV_CHECK/in"
