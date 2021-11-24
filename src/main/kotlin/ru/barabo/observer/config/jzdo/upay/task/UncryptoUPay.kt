package ru.barabo.observer.config.jzdo.upay.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.jzdo.upay.UPayConfig
import ru.barabo.observer.config.jzdo.upay.task.SftpLoad.archivePathToday
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.CryptoPro
import java.io.File
import java.time.Duration
import java.time.LocalTime
import java.util.*

object UncryptoUPay : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::archivePathToday))

    override val accessibleData: AccessibleData
            = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun name(): String = "UPay Расшифровать"

    override fun config(): ConfigTask = UPayConfig

    fun uncryptoFolder() = "${archivePathToday().absolutePath}/uncrypto".byFolderExists()

    override fun processFile(file: File) {
        val decodeName =
            if (file.extension.lowercase(Locale.getDefault()) == "enc") file.nameWithoutExtension else "${file.name}_dec"

        val decodeFile = File("${uncryptoFolder().absolutePath}/$decodeName")

        CryptoPro.decode(file, decodeFile)

        if(!decodeFile.exists()) throw Exception("decode file not found ${decodeFile.absolutePath}")

        val unsignName =
            if (decodeFile.extension.lowercase(Locale.getDefault()) == "sgn") decodeFile.nameWithoutExtension else file.name.substringBefore(
                '.'
            )

        val unsignFile = File("${uncryptoFolder().absolutePath}/$unsignName")

        CryptoPro.unsign(decodeFile, unsignFile)

        if(unsignFile.exists()) {
            decodeFile.delete()
        }
    }

}