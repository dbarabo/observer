package ru.barabo.observer.config.skad.crypto.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.EsProcess
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
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

object Uncrypto550pScad :  FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(Uncrypto550pScad::class.java)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( ::folder550pIn,"CB_ES550P_........_...\\.XML", isModifiedTodayOnly = false))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(10))

    override fun config(): ConfigTask = ScadConfig

    override fun name(): String = "639-П Расшифровать Scad"

    private val X550 = "X:/639-П".ifTest("C:/639-П")

    fun folder550pOut() = "$X550/Out/${todayFolder()}".byFolderExists()

    private fun folder550pIn() = "$X550/In/${todayFolder()}".byFolderExists()

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun processFile(file: File) {
        val uncryptoXml = ScadComplex.fullDecode440p(file, file)

        val outXmlFile = EsProcess.process(uncryptoXml)

        val archiveFileName = File("${folder550pOut()}/${archiveName()}.arj")

        Archive.addToArj(archiveFileName.absolutePath, outXmlFile)

        //uncryptoXml.copyTo(File("$GIS_GMP_639PATH/${uncryptoXml.name}"))
    }

    //private const val GIS_GMP_639PATH = "\\\\gis_gmp/IN"

    private fun archiveName(): String
            = AfinaQuery.execute(query = EXEC_ADD_TO_ARCHIVE, outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

    private const val EXEC_ADD_TO_ARCHIVE = "{ call od.PTKB_440P.addFileToArchive550p( ? ) }"
}