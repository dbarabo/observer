package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.SMEV_CHECK
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.SMEV_440P_IN_FOLDER
import ru.barabo.observer.config.cbr.ticket.task.SMEV_UNO403_IN_FOLDER
import ru.barabo.observer.config.cbr.ticket.task.X440P
import ru.barabo.observer.config.cbr.ticket.task.getFolder440p
import ru.barabo.observer.config.cbr.ticket.task.smev440pInToday
import ru.barabo.observer.config.cbr.ticket.task.smevUno403InToday
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

    override fun config(): ConfigTask = EnsConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false,
        LocalTime.of(2, 0), LocalTime.of(23, 30), Duration.ofSeconds(10))

    override val fileFinderData: List<FileFinderData> =
        listOf(
            FileFinderData(SMEV_IN,"AFN_MIFNS00_0507717_\\d{8}_\\d{5}\\.zip", isModifiedTodayOnly = false),
            FileFinderData(SMEV_440P_IN_FOLDER,".*\\.zip", isModifiedTodayOnly = false),
            FileFinderData(SMEV_UNO403_IN_FOLDER,".*\\.zip", isModifiedTodayOnly = false)
    )

    override fun processFile(file: File) {

        val newArchivePath = getPathForZipFile(file.name)

        val newArchiveFile = File("$newArchivePath/${file.name}")

        file.copyTo(newArchiveFile, overwrite = true)
        file.delete()

        val fnsContainerId =
            AfinaQuery.selectValue(SELECT_FNS_CONTAINER, arrayOf(file.name.uppercase())) as String

        val filesInsideZip = Archive.extractFromZip(newArchiveFile, newArchivePath)

        filesInsideZip?.addFilesToContainer(fnsContainerId)

        filesInsideZip?.filter { it.extension.uppercase()  == "ARJ" }?.forEach {

            val files = Archive.extractFromArj(it, newArchivePath)

            files?.addFilesToContainer(fnsContainerId)
        }

        if(file.exists()) {

            BaraboSmtp.send(to=BaraboSmtp.AUTO, subject="${name()} - Не удалось удалить файл",
                body = "${name()} - Не удалось удалить файл $file")
        }
    }
}

private fun getPathForZipFile(zipFilenameFromFns: String): String {
    return when (zipFilenameFromFns.substring(0..2)) {
        "AFN" -> smevInToday().absolutePath

        "FNS" -> smev440pInToday().absolutePath

        "UNS" -> smevUno403InToday().absolutePath

        else -> throw Exception("неизвестный тип входящего архива")
    }
}

private fun Array<File>.addFilesToContainer(fnsContainerId: String) {
    this.forEach { AfinaQuery.execute(ADD_FILE_TO_CONTAINER,
        arrayOf(fnsContainerId, it.name.uppercase())) }
}

fun smevInToday() = "${getFolder440p().absolutePath}/$SMEV_CHECK".byFolderExists()

val SMEV_IN: String = "$X440P/$SMEV_CHECK/in"

private const val SELECT_FNS_CONTAINER = "select QUERY_ID from od.PTKB_FNS_CONTAINER where upper(FILENAME) = ?"

private const val ADD_FILE_TO_CONTAINER = "{ call OD.PTKB_440P.addFileToFnsContainer(?, ?) }"
