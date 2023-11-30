package ru.barabo.observer.config.fns.ens.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.db.TemplateQuery
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.barabo.p440.task.RooWaitCancel
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.cbr.ticket.task.X440P
import ru.barabo.observer.config.fns.ens.EnsConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.finder.isModifiedMore
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

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

        var startCheckDay = LocalDate.now().minusDays(100)

        val search = Pattern.compile(".*\\.vrb", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val filesAbsent = ArrayList<File>()

        while (startCheckDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() <
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()) {

            val path = startCheckDay.folder440pByDate()

            startCheckDay = startCheckDay.plusDays(1)


            val notFoundFiles = path.listFiles { f ->
                (!f.isDirectory) &&
                        (search.isFind(f.name)) &&
                        (isNotFoundInBase(f.nameWithoutExtension.uppercase()))
            }

            if (notFoundFiles != null && notFoundFiles.isNotEmpty()) {

                logger.error(notFoundFiles.joinToString(" \n\r",  "", ""))

                filesAbsent.addAll(notFoundFiles)
            }
        }

        if(filesAbsent.isNotEmpty()) {
            val info = filesAbsent.joinToString(" \n\r",  "", "")

            BaraboSmtp.sendStubThrows(
                to = BaraboSmtp.AUTO,
                subject = "Не загружены найденные файлы по 440-П",
                body = info
            )
        }

        return State.OK
    }
}

private val logger = LoggerFactory.getLogger(CheckNotLoaded440pFiles::class.java)

fun isNotFoundInBase(fileNameWithoutExt: String): Boolean {
    //logger.error(fileNameWithoutExt)
    return AfinaQuery.selectValue(SELECT_ID_BY_FILE, arrayOf<Any?>("$fileNameWithoutExt.XML")) == null
}

private const val SELECT_ID_BY_FILE = "select f.id from od.ptkb_440p_fns_from f where f.file_name = ?"

fun LocalDate.folder440pByDate(): File = "X:/440-П/${this.dateFolder()}/Получено".byFolderExists()

fun LocalDate.dateFolder(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(this)