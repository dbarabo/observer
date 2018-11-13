package ru.barabo.observer.config.cbr.other.task.cec

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import java.io.File
import java.io.FileInputStream

class XmlCecLoader<E> {

    private fun xstream() : XStream {

        val xstream = XStream(DomDriver())

        xstream.processAnnotations(FileXml::class.java)
        xstream.processAnnotations(Person::class.java)
        xstream.processAnnotations(PersInfo::class.java)
        xstream.processAnnotations(Fio::class.java)
        xstream.processAnnotations(Doc::class.java)
        xstream.processAnnotations(Adress::class.java)
        xstream.processAnnotations(Zapros::class.java)
        xstream.processAnnotations(SlugaInfo::class.java)
        xstream.processAnnotations(IdInfoSluga::class.java)
        xstream.processAnnotations(NameInfoSluga::class.java)

        xstream.useAttributeFor(String::class.java)
        xstream.useAttributeFor(Int::class.java)

        return xstream
    }

    fun load(file : File): E {

        val fileInputStream = FileInputStream(file)

        @Suppress("UNCHECKED_CAST")
        val xml = xstream().fromXML(fileInputStream) as E

        fileInputStream.close()

        return xml
    }
}