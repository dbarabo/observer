package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.TaskMapper
import ru.barabo.xls.ExcelSimple
import java.io.File
import java.time.LocalTime

object OutRegisterAquiring: SingleSelector {
    override val select: String = "select m.id, m.file_name from od.ptkb_ctl_mtl m " +
            "where m.created >= trunc(sysdate) and m.check_count_transact != 0 and m.state = 1 " +
            "and substr(m.file_name, 1, 3) = 'MTL'"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(17, 30))

    override fun name(): String = "Отправить реестры по эквайрингу"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        val terminals = AfinaQuery.selectCursor(SELECT_TERMINALS, arrayOf(elem.idElem))

        if(terminals.isNotEmpty()) {
            processTerminals(terminals, elem.idElem!!)
        }

        return State.OK
    }

    private const val SELECT_TERMINALS = "{? = call od.PTKB_PLASTIC_TURN.selectAquiringTerminals( ? ) }"

    private fun processTerminals(terminals: List<Array<Any?>>, idMtl: Number) {

        val titleVar = createTerminalVar()

        for (terminal in terminals) {

            val terminalId = terminal[0] as String

            titleVar.putTitleData(terminal)

            val file = getFileName(terminal)

            val excelProcess = ExcelSimple(file, TEMPLATE_REGISTER).createTitle(titleVar)

            val transfers = AfinaQuery.selectCursor(SELECT_TRANSFERS, arrayOf(idMtl, terminalId))

            if(transfers.isEmpty()) continue

            createRegister(excelProcess, transfers, terminalId, idMtl)

            sendMailRegister(terminal, file)
        }
    }

    private const val SELECT_TRANSFERS = "{? = call od.PTKB_PLASTIC_TURN.selectAquiringTransfers( ?, ? ) }"

    private const val SELECT_TRANSACT_TRANSFER = "{? = call od.PTKB_PLASTIC_TURN.getRegisterByTransactCtl( ?, ?, ? ) }"

    private fun createRegister(excelProcess: ExcelSimple, transfers: List<Array<Any?>>, terminalId: String, idMtl: Number) {

        val transferVar = createTransferVar()

        transfers.forEach {

            transferVar.putHeader(it)

            excelProcess.createHeader(transferVar)

            val isPay = if(it[6].toString() == "Возврат") 4 else 2

            val transactions =
                    AfinaQuery.selectCursor(SELECT_TRANSACT_TRANSFER, arrayOf(idMtl, terminalId, isPay) /*arrayOf(transferId)*/)

            transferVar["SUM_LOOP"] = createTransactData(excelProcess, transactions)

            excelProcess.createTail(transferVar)
        }

        excelProcess.save()
    }

    private fun createTransactData(excelProcess: ExcelSimple, transactions: List<Array<Any?>>): Double {

        var sumData  = 0.0

        val transactVar = createTransactVar()

        transactions.forEach {

            transactVar.putBodyRow(it)

            excelProcess.createBodyRow(transactVar)

            sumData += (transactVar["amount_loop"] as Number).toDouble()
        }

        return sumData
    }

    private fun MutableMap<String, Any>. putTitleData(terminal: Array<Any?>) {

        this["TerminalId"] = terminal[0]?.let { it as String}?:""

        this["dtStart"] = terminal[1]?.let { it as String}?:""

        this["dtEnd"] = terminal[2]?.let { it as String}?:""

        this["CompanyName"] = terminal[3]?.let { it as String}?:""

        this["adressCompany"] = terminal[4]?.let { it as String}?:""
    }

    private fun getFileName(terminalInfo: Array<Any?>): File {

        val terminal = terminalInfo[0]?.let { it as String}?:""

        val start = (terminalInfo[1] as String).replace("[.:\\s]".toRegex(), "")

        val end = (terminalInfo[2] as String).replace("[.:\\s]".toRegex(), "")

        return File("${IbiSendToJzdo.hCardOutSentTodayFolder()}/Register_${terminal}_${start}_$end.xls")
    }

    private fun MutableMap<String, Any>.putHeader(transfer: Array<Any?>) {

        this["NUM_DOC"] = transfer[1]?.let { it as String } ?: ""

        this["DATE_DOC"] = transfer[2]?.let { it as String } ?: ""

        this["SUM_DOC"] = transfer[3]?.let { (it as Number).toDouble()}?:0.0

        this["DATE_COM"] = transfer[4]?.let { it as String }?:""

        this["SUM_KOM"] = transfer[5]?.let { (it as Number).toDouble() }?:0.0

        this["TYPE_DOC"] = transfer[6]?.let { it as String }?:""
    }

    private fun MutableMap<String, Any>.putBodyRow(transact: Array<Any?>) {

        val index = this["index"]  as Number
        this["index"] = index.toInt() + 1

        this["name_loop"] = transact[0]?.let { it as String}?:""

        this["date_loop"] = transact[1]?.let { it as String}?:""

        this["amount_loop"] = transact[2]?.let { (it as Number).toDouble()}?:0.0
    }

    private fun createTransferVar(): MutableMap<String, Any> = mutableMapOf(
            "SUM_LOOP" to 0.0,
            "DATE_DOC" to "",
            "SUM_DOC" to 0.0,
            "DATE_COM" to "",
            "SUM_KOM" to 0.0
    )

    private fun createTransactVar(): MutableMap<String, Any> = mutableMapOf(
            "index" to 0,
            "name_loop" to "",
            "date_loop" to "",
            "amount_loop" to 0.0)

    private val TEMPLATE_REGISTER = File("${Cmd.LIB_FOLDER}/acquir.xls")

    private fun createTerminalVar(): MutableMap<String, Any> = mutableMapOf(
            "dtStart" to "",
            "dtEnd" to "",
            "TerminalId" to "",
            "CompanyName" to "",
            "adressCompany" to "")

    private fun sendMailRegister(terminalInfo: Array<Any?>, file: File) {
        val mailTo = if(TaskMapper.isAfinaBase()) terminalInfo[5] as? String else null

        val mailCc = if(TaskMapper.isAfinaBase()) terminalInfo[6] as? String else null

        if(!TaskMapper.isAfinaBase() || mailTo != null || mailCc != null) {

            BaraboSmtp.sendStubThrows(to = mailTo?.let { arrayOf(mailTo) }?: emptyArray(),
                    cc = mailCc?.let { arrayOf(mailCc) }?: emptyArray(),
                    bcc = BaraboSmtp.YA,
                    subject = SUBJECT_REGISTER,
                    body = BODY_REGISTER,
                    attachments = arrayOf(file)
            )
        }
    }

    private const val SUBJECT_REGISTER = "Реестр транзакций"

    private const val BODY_REGISTER = "Реестр транзакций по эквайринговым операциям в POS-терминале. Реестр находится во вложении"
}