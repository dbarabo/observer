package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.archive.Archive
import ru.barabo.cmd.Cmd
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.BufferedInputStream
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object LoadBik : Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(21, 0), LocalTime.of(23, 30), Duration.ZERO)

    override fun name(): String = "Загрузка Биков"

    override fun config(): ConfigTask = CryptoConfig

    override fun execute(elem: Elem): State {

        val bikZip = downloadFileBik()

        Archive.upPackFromZip(bikZip.absolutePath)

        bikZip.parentFile.moveToDatFolder()

        Cmd.execDos(EXEC_REINDEX)

        copyToBackup()

        moveToHdat()

        copyToTrans32()

        return State.OK
    }

    private fun downloadFileBik(): File {
        val zipFilePath = "${localBikPath()}/${fileBik()}"

        File(zipFilePath).let { if (it.exists()) it.delete() }

        val zipFile = Files.createFile(Paths.get(zipFilePath))

        Files.newOutputStream(zipFile).use { out ->
            URL(uriBik()).openStream().use { inUri ->
                BufferedInputStream(inUri).use { origin ->
                    origin.copyTo(out)
                }
            }
        }

        return File(zipFilePath)
    }

    private fun moveToHdat() {

        H_BNK_DAT.listFiles().forEach { it.delete() }

        DAT_FOLDER.listFiles()?.forEach {
            it.copyTo(File("${H_BNK_DAT.absolutePath}/${it.name}"), true)

            it.delete()
        }
    }

    private fun copyToBackup() {

        backupClear()

        DAT_FOLDER.listFiles()?.forEach { it.copyTo(File("${BACKUP_DAT.absolutePath}/${it.name}"), true) }
    }

    private fun backupClear() = BACKUP_DAT.listFiles().forEach { it.delete() }

    private fun File.moveToDatFolder() {

        DAT_FOLDER.listFiles()?.forEach { it.delete() }

        listFiles()?.forEach {
            it.copyTo(File("$DAT_FOLDER/${it.name}"), true)
            it.delete()
        }

        this.delete()
    }

    private fun copyToTrans32() {
        val backupSeek = File("${BACKUP_DAT.absolutePath}/$BNK_SEEK")

        backupSeek.copyTo(File("$TRANS32_PATH/$BNK_SEEK"), true)
    }

    private const val TRANS32_PATH = "\\\\gis_gmp/c$/BNK_SEEK"

    private const val BNK_SEEK = "bnkseek.dbf"

    private val H_BNK_DAT = "H:/BNK/DAT".byFolderExists()

    private val BACKUP_DAT = "H:/BNK/Backup/DAT".byFolderExists()

    private val BNK_FOLDER = "${Cmd.LIB_FOLDER}\\bnk"

    private val EXEC_REINDEX = "C: && cd $BNK_FOLDER && rei.cmd"

    private val DAT_FOLDER = "$BNK_FOLDER/DAT".byFolderExists()

    private fun fileTodayMask() = DateTimeFormatter.ofPattern("ddMMyyyy").format(LocalDate.now())

    private fun localBikPath() = Cmd.tempFolder("b")

    private fun uriBik() = "http://cbr.ru/vfs/mcirabis/BIK/${fileBik()}"

    private fun fileBik() = "bik_db_${fileTodayMask()}.zip"
}