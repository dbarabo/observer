package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.db.SessionException
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.task.ToUncrypto440p
import ru.barabo.observer.config.cbr.ticket.task.getFolder440p
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.io.IOException
import java.time.Duration
import java.time.LocalTime

object UncryptoScad440p : FileProcessor, FileFinder {
    override fun name(): String = "Расшифровать scad vrb-файлы 440-П"

    override fun config(): ConfigTask = CryptoScad // ScadConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
            false, LocalTime.MIN, LocalTime.of(23, 0)/*LocalTime.MAX*/, Duration.ofSeconds(1))

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::getFolder440p, ".*\\.vrb"))

    override fun processFile(file: File) {

        val decodeFile = File("${ToUncrypto440p.getUncFolder440p().absolutePath}/${file.nameWithoutExtension}.xml")

        try {
            ScadComplex.fullDecode440p(file, decodeFile)
        } catch (e: IOException) {
            val elem = StoreSimple.findElemByFile(file.name, file.parent, this)
                ?: throw SessionException("elem file=$file not found")

            elem.error = e.message //  "Ошибка расшифрования файла. Данный запрос не будет обработан. В ФНС отправится PB1 с ошибкой расшифрования"
            BaraboSmtp.errorSend(elem)

            file.copyTo(decodeFile, true)
            file.delete()
        }
    }
}
