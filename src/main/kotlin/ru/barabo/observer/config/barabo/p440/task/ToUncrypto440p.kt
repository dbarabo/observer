package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.task.UnCryptoPacket
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.getFolder440p
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration
import java.time.LocalTime

object ToUncrypto440p : FileProcessor, FileFinder {

    override fun name(): String = "В пакет на Расшифровку"

    override fun config(): ConfigTask = P440Config

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::getFolder440p, ".*\\.vrb"))

    fun getUncFolder440p() : File = "${getFolder440p().absolutePath}/uncrypto".byFolderExists()

    override fun processFile(file: File) {

        UnCryptoPacket.addFileToPacket(file, getUncFolder440p(), true,"xml")
    }
}