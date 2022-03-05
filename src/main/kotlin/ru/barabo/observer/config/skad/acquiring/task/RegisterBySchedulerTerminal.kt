package ru.barabo.observer.config.skad.acquiring.task

import ru.barabo.cmd.Cmd
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo
import ru.barabo.observer.config.cbr.sender.SenderMail
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.mail.smtp.onlyAfina
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.xls.*
import java.io.File
import java.time.LocalTime

object RegisterBySchedulerTerminal : SingleSelector {

    override fun name(): String = "Реестры по расписанию"

    override fun config(): ConfigTask = SenderMail

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

        sendMailInfo(fileRegister, vars, terminalTime)
    }

    private fun sendMailInfo(fileRegister: File, vars: List<Var>, terminalTime: String) {

        val terminalCur = vars.firstOrNull { it.name == "TERMINAL_CUR" } ?: throw Exception("var is not found TERMINAL_CUR")

        val cursor = terminalCur.value.value as? CursorData ?: throw Exception("var TERMINAL_CUR is not cursor type")

        val email =  cursor.getColumnResult("EMAIL_CLIENT").getVar().value as? String ?: return

        if(email.indexOf('@') < 0) return

        BaraboSmtp.sendStubThrows(to = arrayOf(email).onlyAfina(), // bcc = BaraboSmtp.YA,
                subject = subjectRegister(terminalTime), body = BODY_INFO, attachments = arrayOf(fileRegister))
    }

    private fun xlsFileName(terminalTime: String): File =
            File("${IbiSendToJzdo.hCardOutSentTodayFolder()}/register$terminalTime.xls")

    private val TEMPLATE_REGISTER = File("${Cmd.LIB_FOLDER}/acquir-time.xls")

    private fun subjectRegister(terminalTime: String) = "Реестр транзакций за раб. смену по терминалу $terminalTime"

    private val BODY_INFO = """
    Здравствуйте! 
    С текущего момента мы поменяли схему перечисления платежй по эквайринговым транзакционным операциям.
Сейчас реестры транзакций будут идти в хронологическом порядке, ежедневно в период "рабочей смены". 
Банковские платежные поручения будут отражать состав реестра. Изначально, время рабочей смены задано с 19-00 предыдущего 
дня до 19-00 текущего дня. Реестры и платежное поручение формируется ч/з 2 часа после окончания смены. 
Это время нужно для того, чтобы получить весь список транзакций за период смены от всех платежных систем.
Вы можете сами себе назначить расписание рабочей смены. Более того за сутки можно назначить до 3-х рабочих смен.
Тогда реестры будут приходить ч/з 2 часа после окончания каждой рабочей смены за период каждой из смен. 
Платежные документы от банка также будут поступать за каждую из смен., т.е. 3 раза в сутки.
Это еще не все. Зачастую, клиенту требуются средства на р/счет еще до окончания смены, поэтому для каждой из рабочих смен
 возможно задать еще время для "промежуточного" получения средств. Т.е. Вам поступит в это время платежное поручение, 
но поскольку рабочая смена еще не будет закончена, то реестр придет только с окончанием смены 
и в нем будут указаны 2 платежных поручения - промежуточное и окончательное. 
Расписание задается отдельно на каждый терминал и в любое время может быть изменено. 

    Средства и реестры будут перечисляться клиенту ежедневно, включая выходные и праздничные дни.

    Чтобы проще было это понять, приведем пример. 
Например, у Вас компания работает с клиентами с 8-00 и до 18-00. 
Логично, что рабочую смену для получения реестров, Вам нужно  установить с 8-00 и до 18-00. 
Тогда реестры будут приходить за указанный период. Т.к. банку нужно минимум 2 часа, чтобы получить всю информацию по терминалам,
то реестры за период с 8-00 до 18-00 будут формироваться в 20-00 и средства Вам за эквайринг будут перечисляться в 20-00.
Но Вам бы хотелось получать средства не в 20-00, а в середине дня, чтобы сразу ими распорядиться. 
Например в 14-00. Тогда Вы указываете еще время для промежуточной платежки в 14-00. 
В 14-00 банк Вам перечислит все средства, которые уже прошли по Вашему терминалу, но еще не были перечислены, а в 20-00 все оставшиеся за смену.
Для указания такого расписания клиенту достаточно указать:
Время окончания рабочей смены: 20-00
Время промежуточного платеж. поручения: 14-00""".trimIndent()

}