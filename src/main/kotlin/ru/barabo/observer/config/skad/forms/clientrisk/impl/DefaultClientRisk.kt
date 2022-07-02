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

class DefaultClientRisk(private val typeClient: Int?) : ClientRisk {

    override fun createFile(): File {

        val file = File("${folderReportToday().absolutePath}/$fileName")

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

private const val fileName = "KYC.xml"

private const val xsd =  "/xsd/kyc.xsd"

private const val SELECT_CLIENT_RISK = "{ ? = call od.XLS_REPORT_ALL.getRiskClientByCbr(?) }"