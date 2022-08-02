package ru.barabo.observer.config.skad.forms.clientrisk.impl

import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.skad.crypto.p311.validateXml
import ru.barabo.observer.config.skad.forms.clientrisk.ClientRisk
import ru.barabo.observer.config.skad.plastic.task.saveXml
import ru.barabo.observer.config.task.clientrisk.ClientForm
import ru.barabo.observer.config.task.clientrisk.MainElement
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DefaultClientRisk(private val typeClient: Int?) : ClientRisk {

    override fun createFile(folderReport: String?): File {

        val folderReportToday = folderReport ?: folderReportToday().absolutePath

        val file = File("$folderReportToday/${fileName()}")

        val xmlData = MainElement(clientForms() )

        saveXml(file, xmlData, "UTF-8", true)

        validateXml(file, xsd, ::errorFolder )

        return file
    }

    override fun clientForms(): List<ClientForm> {

        return AfinaQuery.selectCursor(SELECT_CLIENT_RISK, arrayOf(typeClient))
            .map { ClientForm( ((it[0] as Number).toInt() == 1), it[1] as String, it[2] as String) }
    }

    private fun folderReportToday() = Cmd.tempFolder("ri")

    private fun errorFolder(): File = "${ru.barabo.observer.config.skad.forms.form310.impl.folderReportToday()}/ERROR".byFolderExists()
}

private fun fileName(): String = "KYCCL_2540015598_0021_${todayDate()}_${fileNumberDayFormat()}.xml"

private fun fileNumberByDay(): Int = (AfinaQuery.selectValue(SELECT_COUNT_SEND_FILES)!! as Number).toInt() + 1

private fun fileNumberDayFormat(): String = DecimalFormat("000000").format(fileNumberByDay() )

private fun todayDate(): String = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now())


//private const val fileName = "KYC.xml"

private const val xsd =  "/xsd/kyc.xsd"

private const val SELECT_CLIENT_RISK = "{ ? = call od.XLS_REPORT_ALL.getRiskClientByCbr(?) }"

private const val SELECT_COUNT_SEND_FILES = """
select count(*)
from od.ptkb_form_310_request rq
where rq.type_form = 1
and rq.state = 1
and trunc(rq.updated) = trunc(sysdate)
"""