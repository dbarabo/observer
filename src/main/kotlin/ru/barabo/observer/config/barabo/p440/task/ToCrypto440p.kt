package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.task.CryptoPacket
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.Duration
import java.time.LocalTime

object ToCrypto440p: FileProcessor, FileFinder {

    override fun name(): String = "В пакет на Шифрование"

    override fun config(): ConfigTask = P440Config

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::sendFolder440p, "B(VD|VS|NS|NP|OS).*\\.xml"))

    fun sendFolder440p() = File(GeneralCreator.sendFolder440p())

    fun getCryptoFolder440p() : File = File("${GeneralCreator.sendFolder440p()}/crypto")

    override fun processFile(file: File) {

        CryptoPacket.addFileToPacket(file, getCryptoFolder440p(), true,"vrb" )
    }
}
