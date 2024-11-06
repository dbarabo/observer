package ru.barabo.observer.config.skad.anywork.task

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import com.thoughtworks.xstream.security.AnyTypePermission
import ru.barabo.observer.config.task.clientrisk.fromcbr.MainRisks
import ru.barabo.observer.config.task.clientrisk.fromcbr.Risk
import ru.barabo.observer.config.task.p407.load.*
import java.io.File
import java.io.FileInputStream

class XmlRfmLoader<E> {
    private fun xstream(): XStream {

        val xstream = XStream(DomDriver())

        xstream.addPermission(AnyTypePermission.ANY)

        xstream.processAnnotations(RfmFile::class.java)
        xstream.processAnnotations(Clients::class.java)
        xstream.processAnnotations(ClientBank::class.java)
        xstream.processAnnotations(ClientBankJuric::class.java)
        xstream.processAnnotations(RequestInfo::class.java)
        xstream.processAnnotations(ExtractRequestInfo::class.java)

        xstream.processAnnotations(ClientBankPhysic::class.java)
        xstream.processAnnotations(PhysicFio::class.java)

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