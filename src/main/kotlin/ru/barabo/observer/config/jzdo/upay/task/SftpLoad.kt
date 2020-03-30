package ru.barabo.observer.config.jzdo.upay.task

import ru.barabo.cmd.Cmd
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.jzdo.upay.UPayConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.sftp.Sftp
import ru.barabo.observer.sftp.SftpSetting
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime
import java.time.temporal.ChronoUnit


object SftpLoad : SinglePerpetual  {

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 5

    override val accessibleData: AccessibleData =  AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(7, 0), workTimeTo = LocalTime.of(22, 0))

    override fun name(): String = "Загрузка файлов с sftp"

    override fun config(): ConfigTask = UPayConfig

    private val sftpSetting = SftpSetting(user = "b000764", host = "10.250.0.13", port = 2222,
            privateKeyPath = "${Cmd.LIB_FOLDER}/privzk.ppk")

    private const val ARCHIVE_PATH = "D:/archive_pay"

    private const val REMOTE_PATH = "/OUT"

    fun archivePathToday() = "$ARCHIVE_PATH/${Get440pFiles.todayFolder()}".byFolderExists()

    override fun execute(elem: Elem): State {

        Sftp.openSftp(sftpSetting).use {
            val files = it.lsFileOnly(REMOTE_PATH)

            if(files.isEmpty()) return super.execute(elem)

            it.downloadFiles(REMOTE_PATH, files, archivePathToday())
        }

        return super.execute(elem)
    }
}