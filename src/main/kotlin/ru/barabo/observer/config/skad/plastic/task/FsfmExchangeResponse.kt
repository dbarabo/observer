package ru.barabo.observer.config.skad.plastic.task

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.Dom4JDriver
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder
import org.dom4j.io.OutputFormat
import ru.barabo.cmd.XmlValidator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.skad.crypto.task.nameDateToday
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.p600.exchange.ExtractRegisterOper
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime
import java.util.*


object FsfmExchangeResponse : SingleSelector {
    override fun name(): String = "600-П Выписка по ВОП"

    override fun config(): ConfigTask = PlasticOutSide

    override val select: String = """
select e.id,  vp.label
from od.PTKB_FSFM_REQUEST_EXCHANGE e 
join od.vpclient vp on vp.classified = e.vpclient
where e.state = 0"""

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(8, 0), workTimeTo = LocalTime.of(15, 50),
            executeWait = Duration.ZERO)

    override fun execute(elem: Elem): State {

        val data = AfinaQuery.selectCursor( SELECT_EXCHANGE600P, arrayOf(elem.idElem) )

        val dateStartEnd = AfinaQuery.select(SELECT_DATE_START_END, arrayOf(elem.idElem))[0]

        val start = dateStartEnd[0] as Date

        val end = dateStartEnd[1] as Date

        val fileRequest = dateStartEnd[2] as String

        XmlSaver.createXml(start, end, fileRequest, data)

        AfinaQuery.execute(EXEC_STATE, arrayOf(elem.idElem))

        return State.OK
    }

    private const val SELECT_EXCHANGE600P = "{ ? = call od.PTKB_PRECEPT.getExchange600p(?) }"

    private const val SELECT_DATE_START_END =
            "select e.request_start, e.request_end, e.FILE_REQUEST from od.PTKB_FSFM_REQUEST_EXCHANGE e where e.id = ?"

    private const val EXEC_STATE = "update od.PTKB_FSFM_REQUEST_EXCHANGE set state = 1, updated = sysdate where id = ?"
}

object XmlSaver {

    fun createXml(start: Date, end: Date, fileRequest: String, data: List<Array<Any?>>) {

        val xmlData = ExtractRegisterOper(data, start, end)

        val file = File("${pathFolder().absolutePath}/${generateFileName(fileRequest.substringBeforeLast('.'))}")

        saveXml(file, xmlData)

        XmlValidator.validate(file, "/xsd/inc_07.xsd")
    }

    private fun saveXml(file: File, xmlData: Any) {

        val outputStream = FileOutputStream(file)

        val writer = OutputStreamWriter(outputStream, Charset.forName("UTF-8"))

        val outPutFormat = OutputFormat()
        outPutFormat.lineSeparator = ""
        outPutFormat.isExpandEmptyElements = true
        outPutFormat.encoding = "UTF-8"

        val d4j = Dom4JDriver(XmlFriendlyNameCoder("_", "_"))
        d4j.outputFormat = outPutFormat

        val xstream = XStream(d4j)
        xstream.autodetectAnnotations(true)

        xstream.toXML(xmlData, writer)

        outputStream.close()
    }

    private fun generateFileName(fileRequest: String): String = "INC_07_${fileRequest}_040507717${nameDateToday()}_001.XML"

    private fun pathFolder() = "H:/ПОД ФТ/comita/in/OUT/${nameDateToday()}".byFolderExists()
}
