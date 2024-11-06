package ru.barabo.observer.config.skad.forms.clientrisk

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import com.thoughtworks.xstream.security.AnyTypePermission
import ru.barabo.observer.config.task.clientrisk.fromcbr.MainRisks
import ru.barabo.observer.config.task.clientrisk.fromcbr.Risk
import java.io.File
import java.io.FileInputStream

class XmlClientRiskLoader<E> {

    private fun xstream(): XStream {

        val xstream = XStream(DomDriver())

        xstream.addPermission(AnyTypePermission.ANY)

        xstream.processAnnotations(MainRisks::class.java)
        xstream.processAnnotations(Risk::class.java)

        xstream.useAttributeFor(String::class.java)
        xstream.useAttributeFor(Int::class.java)

        return xstream
    }

    fun load(file: File): E {

        val fileInputStream = FileInputStream(file)

        @Suppress("UNCHECKED_CAST")
        val xml = xstream().fromXML(fileInputStream) as E

        fileInputStream.close()

        return xml
    }
}