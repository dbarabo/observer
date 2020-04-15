package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import java.io.File
import java.time.LocalTime

object SignScadOnlyFile : SignInOut("X:\\VAL\\FTS\\Sign_FTS") {

    override fun name(): String = "Проставить ЭЦП Scad Fts/Val"
}

object SignScad181UByCbr : SignInOut("X:\\VAL\\CB\\CbrSign") {

    override fun name(): String = "Проставить ЭЦП Scad ЦБР VAL 181-У"

    override fun config(): ConfigTask = OtherCbr
}

object SignScadCbFts181U : SignInOut("X:\\VAL\\CB\\Sign_CB") {

    override fun name(): String = "Проставить ЭЦП Scad ЦБ VAL 181-У"
}

open class SignInOut(private val path: String) :  FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf( FileFinderData( { File("$path\\in") } ) )

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS, isDuplicateName = true,
            workTimeFrom = LocalTime.of(6, 0))

    override fun name(): String = "Проставить ЭЦП Scad In/Out"

    override fun config(): ConfigTask = ScadConfig

    override fun processFile(file: File) {
        val destination = File("$path\\out\\${file.name}")

        ScadComplex.signAndMoveSource(file, srcFolder())
        file.copyTo(destination, true)
        file.delete()
    }
}

fun srcFolder() = "D:\\archive_all\\${Get440pFiles.todayFolder()}".byFolderExists()