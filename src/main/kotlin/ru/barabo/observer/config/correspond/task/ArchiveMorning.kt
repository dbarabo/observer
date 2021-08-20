package ru.barabo.observer.config.correspond.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.correspond.Correspond
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.isModifiedMore
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object ArchiveMorning : SinglePerpetual  {

    override fun name(): String = "Утренняя архивация"

    override fun config(): ConfigTask = Correspond

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 15

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(6, 35), workTimeTo = LocalTime.of(10, 0) )

    override fun execute(elem: Elem): State {

        if(LocalTime.now().hour == 6 && LocalTime.now().minute >= 50) {
            return archivate(elem)
        }

        if(LocalTime.now().hour > 6 && isExistsOldFiles() ) {
            return archivate(elem)
        }

        return super.execute(elem)
    }

    private fun isExistsOldFiles(): Boolean {

        val startDayNow = startDayMilisec()

        return (File(UNSIGN_PATH).listFiles { f ->
            (!f.isDirectory) && (!f.isModifiedMore(startDayNow))
        }?.size ?: 0) > 0
    }

    private fun archivate(elem: Elem): State {

        archivateAny(File(UNSIGN_PATH), archivePathToday())

        archivateAny(File(Z_LOADED), archiveZToday())

        return nextDayState(elem)
    }

    private fun archivateAny(folderFrom: File, folderTo: File) {

        val startDayNow = startDayMilisec()

        folderFrom.listFiles { f ->
            (!f.isDirectory) &&
                    (!f.isModifiedMore(startDayNow) )
        }?.forEach {

            val fileTo = File("${folderTo.absolutePath}/${it.name}")

            it.copyTo(fileTo, true)

            it.delete()
        }
    }

    private fun startDayMilisec(): Long = LocalDateTime.now().withHour(7)
        .withMinute(0).withSecond(0).withNano(0)
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    private fun nextDayState(elem: Elem): State {

        elem.executed = LocalDate.now()
            .plusDays(1)
            .atStartOfDay()
            .plusHours( accessibleData.workTimeFrom.hour.toLong() )
            .plusMinutes(accessibleData.workTimeFrom.minute.toLong())


        return State.NONE
    }
}

private const val PATH_ARCHIVE = "C:/oev/Exg/arc"

private const val Z_LOADED = "z:/loaded"

private const val Z_ARCHIVE = "z:/archive"

private fun archiveZToday() = "$Z_ARCHIVE/${Get440pFiles.todayFolder()}".byFolderExists()

private fun archivePathToday() = "${PATH_ARCHIVE}/${Get440pFiles.todayFolder()}".byFolderExists()