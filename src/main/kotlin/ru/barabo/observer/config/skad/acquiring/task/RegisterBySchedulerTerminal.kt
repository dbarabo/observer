package ru.barabo.observer.config.skad.acquiring.task

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo
import ru.barabo.observer.config.skad.acquiring.Acquiring
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.xls.ExcelSql
import ru.barabo.xls.Var
import ru.barabo.xls.VarResult
import ru.barabo.xls.VarType
import java.io.File
import java.time.LocalTime

object RegisterBySchedulerTerminal : SingleSelector {
    private val logger = LoggerFactory.getLogger(RegisterBySchedulerTerminal::class.java)

    override fun name(): String = "Реестры по расписанию"

    override fun config(): ConfigTask = Acquiring

    override val accessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(0, 2),
            workTimeTo = LocalTime.of(23, 56))

    override fun isCursorSelect(): Boolean = true

    override val select: String = "{ ? = call od.PTKB_PLASTIC_TURN.getExecRealForSendRegister }"

    override fun execute(elem: Elem): State {

        createRegister(elem.idElem!!, elem.name)

        return State.OK
    }

    private fun createRegister(idExecReal: Long, terminalTime: String) {

        val fileRegister = xlsFileName(terminalTime)

        val vars = ArrayList<Var>()

        vars += Var("EXECREALID", VarResult(VarType.INT, idExecReal) )

        val excelSql = ExcelSql(fileRegister, TEMPLATE_REGISTER)

        excelSql.initRowData(vars)

        excelSql.processData()
    }

    private fun xlsFileName(terminalTime: String): File =
            File("${IbiSendToJzdo.hCardOutSentTodayFolder()}/register$terminalTime.xls")

    private val TEMPLATE_REGISTER = File("${Cmd.LIB_FOLDER}/acquir-time.xls")
}