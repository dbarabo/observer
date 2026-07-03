package ru.barabo.observer.config.fns.ens.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.collections.joinToString

object CheckNotLoaded440pFiles : Periodical {

    override fun name(): String = "Проверка незагруженных файлов 440-П"

    override fun config(): ConfigTask =  EnsConfig

    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData=  AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(7, 0), LocalTime.of(18, 0), Duration.ZERO)

    override fun execute(elem: Elem): State {

        val filesAbsent = AfinaQuery.selectCursor(SELECT_ABSENT_FILES)

        if(filesAbsent.isEmpty()) return State.OK

        val info = filesAbsent.joinToString(" \n\r") {  "${it[0]}" }

        BaraboSmtp.sendStubThrows(
                to = BaraboSmtp.YA,
                subject = "Не загружены найденные файлы по 440-П",
                body = info
        )

        return State.OK
    }
}

private const val SELECT_ABSENT_FILES = "{ ? = call od.PTKB_440P.getAbsentFilesInAfina }"


fun LocalDate.dateFolder(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(this)