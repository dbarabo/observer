package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.EsProcess
import ru.barabo.observer.config.cbr.ticket.task.GetProcess550pFiles
import ru.barabo.observer.config.skad.crypto.ScadConfig
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
import java.util.regex.Pattern

object Response550pScad : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( ::folder550pOut,"UV_0021_0000_CB_ES550P_........_001\\.XML", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(30))

    override fun config(): ConfigTask = ScadConfig

    override fun name(): String = "550-П Отправить Scad"

    private fun folder550pOut() = "X:/550-П/Out/${todayFolder()}".byFolderExists()

    private fun folder550pOutSrc() = "X:/550-П/Out/${todayFolder()}/src".byFolderExists()

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    private const val ptkPsdOutPath = "p:"

    override fun processFile(file : File) {
        val xmlOutFiles = folder550pOut().listFiles { f ->
            !f.isDirectory && f.name.indexOf(".XML", ignoreCase = true) > 0
        }

        val archiveFileName = archiveFileName()

        Archive.addToArj(archiveFileName, xmlOutFiles)

        val signedFile = ScadComplex.signAndMoveSource(File(archiveFileName), folder550pOutSrc() )

        val copyOut = File("$ptkPsdOutPath/${signedFile.name}")

        signedFile.copyTo(copyOut, true)
    }

    private fun archiveFileName() :String {

        val search = Pattern.compile("ARH550P_0021_0000_${GetProcess550pFiles.todayBlind()}_...\\.arj",
                Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val max = File(EsProcess.folder550pOut()).listFiles { f ->
            !f.isDirectory && search.matcher(f.name).find()}
                ?.map { Integer.parseInt(it.name.substring(it.name.indexOf(".") - 3, it.name.indexOf(".")))}?.max()?:0 + 1

        return "${folder550pOut()}/ARH550P_0021_0000_${GetProcess550pFiles.todayBlind()}_00$max.arj"
    }
}