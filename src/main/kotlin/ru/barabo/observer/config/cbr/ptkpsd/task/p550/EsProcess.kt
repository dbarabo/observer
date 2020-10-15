package ru.barabo.observer.config.cbr.ptkpsd.task.p550

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.load.CbEs550pXml
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.save.Kvit550
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.save.RecordEs
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.save.RecordEsList
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object EsProcess {

    fun process(inXml :File): File {

        val esIn = XmlLoader<CbEs550pXml>().load(inXml)

        return generateResponse(esIn, inXml)
    }

    fun folder550pOut() :String = "X:/639-П/Out/${todayFolder()}"

    const val PREFIX_TICKET = "UV_0021_0000_"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    private fun ticketFile(file :File) = File("${folder550pOut()}/$PREFIX_TICKET${file.name}")

    private fun generateResponse(esIn: CbEs550pXml, inXml: File): File {

        val ticket550 = Kvit550(esIn, inXml)

        return ticketFile(inXml).apply { saveXml(ticket550, this) }
    }

    private const val HEADER_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"

    private fun saveXml(ticket550 :Kvit550, outXml :File) {

        if(!outXml.parentFile.exists()) {
            outXml.parentFile.mkdirs()
        }

        val outputStream = FileOutputStream(outXml)

        val writer = OutputStreamWriter(outputStream, Charset.forName("UTF-8"))

        writer.write(HEADER_UTF8)

        xstream().toXML(ticket550, writer)

        outputStream.close()
    }

    private fun xstream(): XStream {
        val xstream = XStream(DomDriver("UTF-8", XmlFriendlyNameCoder("_-", "_")))

        xstream.useAttributeFor(Long::class.java)
        xstream.useAttributeFor(RecordEs::class.java, "idInfo")
        xstream.processAnnotations(Kvit550::class.java)
        xstream.processAnnotations(RecordEs::class.java)
        xstream.processAnnotations(RecordEsList::class.java)

        return xstream
    }
}