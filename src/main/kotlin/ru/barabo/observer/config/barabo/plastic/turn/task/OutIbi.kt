package ru.barabo.observer.config.barabo.plastic.turn.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.sql.Clob
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object OutIbi: Periodical {
    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val count: Long = 5

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.of(7, 0))

    override fun name(): String = "Выгрузка оборотов"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        val data = AfinaQuery.execute(query = EXEC_TURN_OUT,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        val isExistsTurn = data?.let { it.size == 2 && it[1] != null }?:false

        if(isExistsTurn) {
            saveFile(data!![1] as String, (data[0] as Clob).clob2string() )
        }

        val dataDelete = AfinaQuery.execute(query = EXEC_TURN_OUT_DELETE,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        val isExistsTurnDelete = dataDelete?.let { it.size == 2 && it[1] != null }?:false

        if(isExistsTurnDelete) {
            saveFile(dataDelete!![1] as String, (dataDelete[0] as Clob).clob2string() )
        }

        return State.OK
    }

    val hCardOut = if(TaskMapper.isAfinaBase())"H:/КартСтандарт/out" else "C:/КартСтандарт/out"

    fun hCardOutToday() = "$hCardOut/${Get440pFiles.todayFolder()}"

    fun hCardOutTodayFolder() :File = hCardOutToday().byFolderExists()

    private val EXEC_TURN_OUT = "{ call od.PTKB_PLASTIC_TURNOUT.turnOut(?, ?) }"

    private val EXEC_TURN_OUT_DELETE = "{ call od.PTKB_PLASTIC_TURNOUT.turnOutDelete(?, ?) }"

    private fun saveFile(fileName: String, dataFile: String) {

        val file = File("${hCardOutTodayFolder().absolutePath}/$fileName")

        if(file.exists()) throw IOException("file already exists $file")

        file.writeText(dataFile, Charset.forName("CP1251"))
    }
}