package ru.barabo.observer.config.skad.anywork.task

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.skad.anywork.AnyWork
import ru.barabo.observer.config.skad.plastic.task.toDate
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p407.load.ClientBank
import ru.barabo.observer.config.task.p407.load.RfmFile
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.sql.Clob
import java.time.Duration

object Extract407pByRfm : FileFinder, ActionTask, Executor {

    override fun name(): String = "Выгрузка выписок RFM 407-П"

    override fun config(): ConfigTask = AnyWork

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY, executeWait = Duration.ZERO)

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData( ::xRfm,
        "RFM_040507717_\\d\\d\\d\\d\\d\\d\\d\\d_0\\d\\d\\.xml"))

    override fun actionTask(): ActionTask = this

    override fun execute(elem: Elem): State {

        val file = elem.getFile()

        if (!file.exists()) throw IOException("file not found ${file.absolutePath}")

        val requestList = loadXmlData(file).takeIf { it.isNotEmpty() } ?: return State.ARCHIVE

        for (request in requestList) {
            extractById(request)
        }
        //extractById(1286099976L)

        return State.OK
    }

    private fun loadXmlData(file: File): List<Number> {
        val fileXml = XmlRfmLoader<RfmFile>().load(file)

        val clients = fileXml.clientList
            .filter { it.requestInfo.extractRequestInfo != null && it.requestInfo.extractRequestInfo?.dateStart != null }
            .takeIf { it.isNotEmpty() } ?: emptyList()

        val idList = ArrayList<Number>()

        for(client in clients) {

            /*val start = client.requestInfo.extractRequestInfo.dateStart.toDate().toTimestamp()
            val end = client.requestInfo.extractRequestInfo.dateEnd.toDate().toTimestamp()

            val params = arrayOf<Any?>(file.nameWithoutExtension, client.clientBankJuric.name,
                client.clientBankJuric.inn, client.clientBankJuric.kpp, start, end)
*/

            val id = AfinaQuery.execute(INS_RFM, params = client.paramsClient(file),
                outParamTypes = intArrayOf(OracleTypes.NUMBER))!![0] as Number

            idList += id
        }

        val moveFile = File("${xRfmToday().absoluteFile}/${file.name}")

        file.copyTo(moveFile, true)
        file.delete()

        return idList
    }

    private fun ClientBank.paramsClient(file: File): Array<Any?> {
        val start = requestInfo.extractRequestInfo.dateStart.toDate().toTimestamp()
        val end = requestInfo.extractRequestInfo.dateEnd.toDate().toTimestamp()

        return if(clientBankJuric == null)
            arrayOf<Any?>(file.nameWithoutExtension, clientBankPhysic.fio.fullName, clientBankPhysic.inn, "", start, end)
        else
            arrayOf<Any?>(file.nameWithoutExtension, clientBankJuric.name, clientBankJuric.inn, clientBankJuric.kpp,
                start, end)
    }

    private fun extractById(id: Number) {

        val (id365p, responseFile) = AfinaQuery.execute(CREATE_365P_RECORD, params = arrayOf<Any?>(id),
            outParamTypes = intArrayOf(OracleTypes.NUMBER, OracleTypes.VARCHAR) )!!

        val textClob = AfinaQuery.execute(
            query = GET_EXTRACT_DATA,
            params = arrayOf(id365p),
            outParamTypes = intArrayOf(OracleTypes.CLOB)
        )!![0] as Clob

        val textFile = File("${xRfmToday().absolutePath}/$responseFile")

        textFile.writeText(textClob.clob2string(), charset = Charset.forName("CP866"))
    }
}

private const val GET_EXTRACT_DATA = "{ call od.PTKB_CEC.createExtractData(?, ?) }"

private const val CREATE_365P_RECORD = "{ call od.PTKB_CEC.create365pRecord(?, ?, ?) }"

private const val INS_RFM = "{ call od.PTKB_CEC.addRfmExtract(?, ?, ?, ?, ?, ?, ?) }"

private const val X_407P = "C:/407P_RFM"
    //"K:/ARH_LEG/407-П (запросы РФМ)     !!! с 12.01.2022 будут ИЗМЕНЕНИЯ/2021_2022_Запросы+Ответы/2023/07/10.07.2023/Запрос"

private fun xRfm() = File("$X_407P")
private fun xRfmToday() = "$X_407P/${todayFolder()}".byFolderExists()




