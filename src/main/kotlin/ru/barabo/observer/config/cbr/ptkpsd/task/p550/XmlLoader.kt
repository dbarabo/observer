package ru.barabo.observer.config.cbr.ptkpsd.task.p550

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.load.*
import java.io.File
import java.io.FileInputStream


class XmlLoader<E> {

    private fun xstream() : XStream {

        val xstream = XStream(DomDriver())

        xstream.processAnnotations(CbEs550pXml::class.java)
        xstream.processAnnotations(ServicePart::class.java)
        xstream.processAnnotations(InfoPart550::class.java)
        xstream.processAnnotations(Part11::class.java)
        xstream.processAnnotations(Part12::class.java)
        xstream.processAnnotations(Part2::class.java)
        xstream.processAnnotations(Part550::class.java)

        return xstream
    }

    fun load(file :File) :E {

        val fileInputStream = FileInputStream(file)

        val xml = xstream().fromXML(fileInputStream) as E

        fileInputStream.close()

        return xml
    }
}