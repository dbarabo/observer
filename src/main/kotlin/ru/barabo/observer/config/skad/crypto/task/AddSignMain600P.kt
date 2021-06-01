package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.text.DecimalFormat
import java.time.Duration
import java.time.LocalTime

object AddSignMain600P : FileFinder, FileProcessor {

    override fun name(): String = "600-П sign Ответ-в Архив"

    override fun config(): ConfigTask = ScadConfig

    override val accessibleData: AccessibleData = AccessibleData( workTimeFrom = LocalTime.of(9, 0),
            workTimeTo = LocalTime.of(23, 0), executeWait = Duration.ofSeconds(1) )

    override val fileFinderData: List<FileFinderData> = listOf( FileFinderData( ::pathMainToday, null ) )

    override fun processFile(file: File) {

        if(file.name.equals("thumbs.db", true)) {
            file.delete()
            return
        }

        val arjArchive = File("${pathCryptoMainToday().absolutePath}/${arjArchiveNameToday()}")

        ScadComplex.signAddArchive600p(file, arjArchive)
    }

    private fun pathMainToday() = File("$pathMain/${nameDateToday()}")

    fun pathCryptoMainToday() = File("${pathMainToday()}/crypto")

    private fun arjArchiveNameToday() = "DIFM_040507717_${nameDateToday()}_${getCountSend()}_000.ARJ"


    private fun getCountSend(): String {
        val number = StoreSimple.getCountByTask(task = CryptoArchive600P, nameContains = "DIFM") + 1

        return DecimalFormat("00").format(number)
    }

    private const val pathMain = "H:/ПОД ФТ/comita/600-П/DIFM"
}