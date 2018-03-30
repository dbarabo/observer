package ru.barabo.observer.config.cbr.other

import ru.barabo.cmd.Cmd
import ru.barabo.cmd.deleteFolder
import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.util.*

object CorrectPrim : SingleSelector {

    override val select: String =
            "select id, to_char(date_report, 'dd.mm.yyyy') from od.ptkb_ptkpsd_101form where state = 0 " +
                    "and upper(TYPE_REPORT) = 'НЕРЕГУЛЯРНАЯ'"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS, executeWait = null)

    override fun name(): String = "Правка показателей ежеднев."

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        val reportDate = AfinaQuery.selectValue(SELECT_DATE, arrayOf(elem.idElem))

        AfinaQuery.execute(EXEC_CORRECT_PRIM, arrayOf(reportDate))

        sendReportCorrect(elem.idElem, reportDate as Date)

        return State.OK
    }

    private const val SELECT_DATE = "select date_report from od.ptkb_ptkpsd_101form where id = ?"

    private const val EXEC_CORRECT_PRIM = "call od.PTKB_PRECEPT.correctPrimBalance(?)"

    private fun sendReportCorrect(idElem :Long?, dateReport: Date) {

        val file = createReportFile(idElem, dateReport)?: return

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = titleHtml(dateReport),
                body = titleHtml(dateReport), attachments = arrayOf(file))

        file.parentFile.deleteFolder()
    }

    private fun createReportFile(idElem :Long?, dateReport: Date): File? {
        val data = createHtmlData(idElem, dateReport) ?: return null

        val tempFolder = Cmd.tempFolder("f100")

        val file = File("${tempFolder.absoluteFile}/$REPORT_FILE")

        file.writeText(data, Charset.forName("CP1251"))

        return file
    }

    private const val REPORT_FILE = "reportPrim101.html"

    private fun createHtmlData(idElem: Long?, dateReport: Date): String? {

        val data = AfinaQuery.selectCursor(CURSOR_REPORT_CORRECT, arrayOf(idElem))

        if(data.isEmpty()) return null

        val content = HtmlContent(titleHtml(dateReport), titleHtml(dateReport), HEADER_TABLE, data)

        return content.html()
    }

    private fun titleHtml(dateReport: Date) = "Измененные показатели на $dateReport"

    private val HEADER_TABLE = mapOf(
            "Счет" to "left",
            "Валюта" to "left",
            "Новый остаток"  to "right",
            "Старый остаток"  to "right",
            "Новый дебет"  to "right",
            "Старый дебет"  to "right",
            "Новый кредит"  to "right",
            "Старый кредит" to "right")

    private const val CURSOR_REPORT_CORRECT = "{ ? = call od.PTKB_PRECEPT.getCorrectPrimView( ? ) }"
}