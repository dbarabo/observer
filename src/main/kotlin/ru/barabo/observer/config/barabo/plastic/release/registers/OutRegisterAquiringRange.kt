package ru.barabo.observer.config.barabo.plastic.release.registers

import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring.createTransactData
import ru.barabo.observer.config.barabo.plastic.turn.task.OutRegisterAquiring.putTitleData
import ru.barabo.observer.config.barabo.plastic.turn.task.sendMailRegister
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.EventAction
import ru.barabo.observer.config.task.template.periodic.IdName
import ru.barabo.observer.config.task.template.periodic.NULL_ID_NAME
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.xls.ExcelSimple
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

abstract class OutRegisterAquiringRange : EventAction {

    override var actionIdNow: IdName = IdName()

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(18, 0), LocalTime.of(21, 0))

    override fun name(): String = "Периодические реестры по эквайрингу"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override fun getEventActionNow(): IdName {

        if(!isSelectActionNow()) return NULL_ID_NAME

        val id = LocalDate.now().dayOfYear

        val name = nameRegister()

        return IdName(id.toLong(), name)
    }

    private fun isSelectActionNow(): Boolean = (AfinaQuery.selectValue(selectActionNow() ) == null)

    abstract fun selectActionNow(): String

    abstract fun nameRegister(): String

    override fun execute(elem: Elem): State {
        val terminals = AfinaQuery.selectCursor(selectCursorTerminals())

        if(terminals.isNotEmpty()) {
            terminals.processTerminals()
        }

        return State.OK
    }

    abstract fun selectCursorTerminals(): String

    private fun List<Array<Any?>>.processTerminals() {
        val titleVar = OutRegisterAquiring.createTerminalVar()

        for (terminal in this) {
            titleVar.putTitleData(terminal)

            val terminalId = terminal[0] as String

            val file = getFileName(terminalId)

            val excelProcess = ExcelSimple(file, TEMPLATE_REGISTER_RANGE).createTitle(titleVar)

            excelProcess.createRegisters(terminalId)

            file.sendMailRegister(terminal, subjectRegister() )
        }
    }

    abstract fun subjectRegister(): String

    private fun ExcelSimple.createRegisters(terminalId: String) {

        createRegisters(terminalId, PAY)

        createRegisters(terminalId, REVERSE)

        save()
    }

    private fun ExcelSimple.createRegisters(terminalId: String, payType: String) {

        val isPay = if(payType == REVERSE) 4 else 2

        val transactions = AfinaQuery.selectCursor(selectCursorTransactRegisters(), arrayOf(terminalId, isPay) )

        if(transactions.isEmpty()) return

        val typePayVar = createTypePay(payType)

        createHeader(typePayVar)

        typePayVar["SUM_LOOP"] =  transactions.createTransactData(this)

        createTail(typePayVar)
    }

    abstract fun selectCursorTransactRegisters(): String

    private fun createTypePay(typePay: String): MutableMap<String, Any> = mutableMapOf(
            "SUM_LOOP" to 0.0,
            "TYPE_DOC" to typePay
    )

    private fun getFileName(terminalId: String): File = File(
            "${IbiSendToJzdo.hCardOutSentTodayFolder()}/RegByPeriod_${terminalId}_${nameRegister()}.xls")
}

private val TEMPLATE_REGISTER_RANGE = File("${Cmd.LIB_FOLDER}/acquir_range.xls")

private const val PAY = "Зачисление"

private const val REVERSE = "Возврат"
