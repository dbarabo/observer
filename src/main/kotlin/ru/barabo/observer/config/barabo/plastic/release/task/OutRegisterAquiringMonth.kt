package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring.putTitleData
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.Periodical
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.xls.ExcelSimple
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring.createTransactData
import ru.barabo.observer.config.barabo.plastic.turn.task.sendMailRegister

object OutRegisterAquiringMonth: Periodical {
    override val unit: ChronoUnit = ChronoUnit.DAYS

    override var count: Long = 1

    override var lastPeriod: LocalDateTime? = null

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(18, 0), LocalTime.of(21, 0))

    override fun name(): String = "Месячные реестры по эквайрингу"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override fun isAccess() = super.isAccess() && isNow4WorkDayOfMonth()

    private fun isNow4WorkDayOfMonth(): Boolean = (AfinaQuery.selectValue(SELECT_4DAY_MONTH) == null)

    private const val SELECT_4DAY_MONTH =
            "select nullif(od.getWorkDayBack(trunc(sysdate), 3), getNextWorkDay(trunc(sysdate, 'MM')-1) ) from dual"

    override fun execute(elem: Elem): State {
        val terminals = AfinaQuery.selectCursor(SELECT_TERMINALS)

        if(terminals.isNotEmpty()) {
            terminals.processTerminals()
        }
        return State.OK
    }

    private const val SELECT_TERMINALS = "{? = call od.PTKB_PLASTIC_TURN.selectMonthAquiringTerminals }"

    private fun List<Array<Any?>>.processTerminals() {
        val titleVar = OutRegisterAquiring.createTerminalVar()

        for (terminal in this) {
            titleVar.putTitleData(terminal)

            val terminalId = terminal[0] as String

            val file = getFileName(terminalId)

            val excelProcess = ExcelSimple(file, TEMPLATE_REGISTER_MONTH).createTitle(titleVar)

            excelProcess.createRegisters(terminalId)

            file.sendMailRegister(terminal, SUBJECT_REGISTER)
        }
    }

    private const val SUBJECT_REGISTER = "Реестр транзакций за месяц"

    private val TEMPLATE_REGISTER_MONTH = File("${Cmd.LIB_FOLDER}/acquir_month.xls")

    private const val PAY = "Зачисление"

    private const val REVERSE = "Возврат"

    private fun ExcelSimple.createRegisters(terminalId: String) {

        createRegisters(terminalId, PAY)

        createRegisters(terminalId, REVERSE)

        save()
    }

    private fun ExcelSimple.createRegisters(terminalId: String, payType: String) {

        val isPay = if(payType == REVERSE) 4 else 2

        val transactions = AfinaQuery.selectCursor(SELECT_TRANSACT_REGISTERS, arrayOf(terminalId, isPay) )

        if(transactions.isEmpty()) return

        val typePayVar = createTypePay(payType)

        createHeader(typePayVar)

        typePayVar["SUM_LOOP"] =  transactions.createTransactData(this)

        createTail(typePayVar)
    }

    private fun createTypePay(typePay: String): MutableMap<String, Any> = mutableMapOf(
            "SUM_LOOP" to 0.0,
            "TYPE_DOC" to typePay
    )

    private const val SELECT_TRANSACT_REGISTERS = "{? = call od.PTKB_PLASTIC_TURN.getRegistersByMonth(?, ?) }"


    private fun getFileName(terminalId: String): File = File(
"${IbiSendToJzdo.hCardOutSentTodayFolder()}/RegByMonth_${terminalId}_${LocalDate.now().formatter()}.xls")

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM")

    private fun LocalDate.formatter(): String = dateTimeFormatter.format(this.minusMonths(1))
}