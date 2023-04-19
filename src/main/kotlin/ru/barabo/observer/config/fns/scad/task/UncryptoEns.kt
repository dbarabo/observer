package ru.barabo.observer.config.fns.scad.task

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.barabo.p440.task.ToUncrypto440p
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.config.barabo.p440.task.smevInToday
import java.io.File
import java.time.Duration
import java.time.LocalTime

object UncryptoEns : FileProcessor, FileFinder {

    private val logger = LoggerFactory.getLogger(UncryptoEns::class.java)
    override fun name(): String = "Расшифровать vrb-файлы ЕНС-smev"

    override fun config(): ConfigTask = CryptoScad

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.ALL_DAYS,
        false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::smevInToday, ".*\\.vrb"))

    override fun processFile(file: File) {

        val decodeFile = File("${getUncryptoFolderSmev().absolutePath}/${file.nameWithoutExtension}.xml")

        logger.error("cryptoFile=$file")
        logger.error("decodeFile=$decodeFile")

        ScadComplex.fullDecode440p(file, decodeFile)
    }
}

fun getUncryptoFolderSmev(): File = "${smevInToday().absolutePath}/uncrypto".byFolderExists()