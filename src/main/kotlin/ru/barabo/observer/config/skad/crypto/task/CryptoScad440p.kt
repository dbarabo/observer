package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator.Companion.sendFolder440p
import ru.barabo.observer.config.cbr.ptkpsd.task.Send440pArchive
import ru.barabo.observer.config.cbr.ticket.task.sentFolderNewSmev440pToday
import ru.barabo.observer.config.cbr.ticket.task.sentFolderNewSmevUno403Today
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.Duration
import java.time.LocalTime

object CryptoScad440p: FileProcessor, FileFinder {

    override fun name(): String = "Зашифровать SCAD 440-П"

    override fun config(): ConfigTask = CryptoScad // ScadConfig

    override val accessibleData: AccessibleData = AccessibleData(
            WeekAccess.ALL_DAYS,
            false,
            LocalTime.MIN,
            LocalTime.of(18, 0), // 17:45,
            Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::sendFolder440p, "B(VD|VS|NS|NP|OS).*\\.xml"))

    override fun processFile(file: File) {

        val smevTypeFile = (AfinaQuery.selectValue(SELECT_SMEV_TYPE_FILE, arrayOf(file.name)) as Number).toInt()

        when(smevTypeFile) {
            OLD_VERSION_FILES, PB_OLD_SMEV_FILES -> cryptoScad(file)

            NEW_SMEV_440P_FILES -> setFileToNew440p(file)

            NEW_SMEV_UNO_373_FILES -> setFileToNewSmev403(file)
        }
    }

    private fun setFileToNew440p(sourceFile: File) {

        val copyFile = File("${sentFolderNewSmev440pToday().absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copyFile, true)

        if(copyFile.exists()) {
            sourceFile.delete()
        }
    }

    private fun setFileToNewSmev403(sourceFile: File) {

        val copyFile = File("${sentFolderNewSmevUno403Today().absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copyFile, true)

        if(copyFile.exists()) {
            sourceFile.delete()
        }
    }

    private fun cryptoScad(file: File) {
        val cryptoScad = File("${Send440pArchive.sendFolderCrypto440p().absolutePath}/${file.nameWithoutExtension}.vrb")

        ScadComplex.fullEncode440p(file, cryptoScad)
    }
}

private const val SELECT_SMEV_TYPE_FILE = "select OD.PTKB_440P.getSmevTypeByFileName( ? ) from dual"

private const val OLD_VERSION_FILES = 0

private const val PB_OLD_SMEV_FILES = 1

private const val NEW_SMEV_440P_FILES = 2

private const val NEW_SMEV_UNO_373_FILES = 3



