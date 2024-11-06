package ru.barabo.observer.config.skad.plastic.task

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.Dom4JDriver
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder
import com.thoughtworks.xstream.security.AnyTypePermission
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
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.p600.exchange.ExtractRegisterOper
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime
import java.util.*


object FsfmExchangeResponse : SingleSelector {
    override fun name(): String = "600-П Выписка по ВОП"

    override fun config(): ConfigTask = PlasticOutSide

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(8, 0), workTimeTo = LocalTime.of(15, 50),
            executeWait = Duration.ZERO)

    override val select: String = """
select e.id,  vp.label
from od.PTKB_FSFM_REQUEST_EXCHANGE e 
join od.vpclient vp on vp.classified = e.vpclient
where e.state = 0"""

    override fun execute(elem: Elem): State {

        val data = AfinaQuery.selectCursor( SELECT_EXCHANGE600P, arrayOf(elem.idElem) )

        val dateStartEnd = AfinaQuery.select(SELECT_DATE_START_END, arrayOf(elem.idElem))[0]

        val start = dateStartEnd[0] as Date

        val end = dateStartEnd[1] as Date

        val fileRequest = dateStartEnd[2] as String

        val fileResponse = XmlSaver.createXml(start, end, fileRequest, data)

        AfinaQuery.execute(EXEC_STATE, arrayOf(elem.idElem))

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, bcc = BaraboSmtp.OPER, subject = "600-П Выписка по ВОП сформирована",
                body = bodyInfo(elem.name, fileRequest, fileResponse), attachments = arrayOf(fileResponse))

        return State.OK
    }

    private fun bodyInfo(name: String, fileRequest: String, fileResponse: File) =
"""Выписка по ВОП для ФСФМ сформирована
Файл запроса: $fileRequest
Клиент: $name
Файл выписки: ${fileResponse.absolutePath}
""".trimIndent()

    private const val SELECT_EXCHANGE600P = "{ ? = call od.PTKB_PRECEPT.getExchange600p(?) }"

    private const val SELECT_DATE_START_END =
            "select e.request_start, e.request_end, e.FILE_REQUEST from od.PTKB_FSFM_REQUEST_EXCHANGE e where e.id = ?"

    private const val EXEC_STATE = "update od.PTKB_FSFM_REQUEST_EXCHANGE set state = 1, updated = sysdate where id = ?"
}

object XmlSaver {

    fun createXml(start: Date, end: Date, fileRequest: String, data: List<Array<Any?>>): File {

        val xmlData = ExtractRegisterOper(data, start, end)

        val file = File("${pathFolder().absolutePath}/${generateFileName(fileRequest.substringBeforeLast('.'))}")


        saveXml(file, xmlData)

        XmlValidator.validate(file, "/xsd/inc_07.xsd")

        return file
    }

    private fun generateFileName(fileRequest: String): String = "INC_07_${fileRequest}_040507717${nameDateToday()}_001.XML"

    private fun pathFolder() = "H:/ПОД ФТ/comita/in/OUT/${nameDateToday()}".byFolderExists()
}

fun saveXml(file: File, xmlData: Any, charset: String = "UTF-8", isUseAttr: Boolean = false) {

    val outputStream = FileOutputStream(file)

    val writer = OutputStreamWriter(outputStream, Charset.forName(charset))

    val outPutFormat = OutputFormat()
    outPutFormat.lineSeparator = ""
    outPutFormat.isExpandEmptyElements = false
    outPutFormat.encoding = charset

    val d4j = Dom4JDriver(XmlFriendlyNameCoder("_", "_"))
    d4j.outputFormat = outPutFormat

    val xstream = XStream(d4j)
    xstream.autodetectAnnotations(true)

    xstream.addPermission(AnyTypePermission.ANY)

    if(isUseAttr) {
        xstream.useAttributeFor(String::class.java)
        xstream.useAttributeFor(Int::class.java)
        xstream.useAttributeFor(Long::class.java)
        xstream.useAttributeFor(Double::class.java)
        xstream.useAttributeFor(BigDecimal::class.java)
        xstream.useAttributeFor(BigInteger::class.java)
        xstream.useAttributeFor(Boolean::class.java)
        xstream.useAttributeFor(Integer::class.java)
    }

    xstream.toXML(xmlData, writer)

    outputStream.close()
}
