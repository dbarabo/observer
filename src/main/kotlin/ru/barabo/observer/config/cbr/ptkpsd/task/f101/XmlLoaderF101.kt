package ru.barabo.observer.config.cbr.ptkpsd.task.f101

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import ru.barabo.observer.config.cbr.f101.Balance
import ru.barabo.observer.config.cbr.f101.Creator
import java.io.File
import java.io.FileInputStream

class XmlLoaderF101<E> {

    private fun processAnnotation(xstream: XStream) {

        //xstream.ignoreUnknownElements()

        xstream.useAttributeFor(String::class.java)
        xstream.useAttributeFor(Int::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.F101Xml::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Creator::class.java)
        xstream.alias("Составитель", Creator::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Balance::class.java)
        xstream.alias("Баланс", Balance::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Data101::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.OffBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.OffBalanceGroup::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.RowDataBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalOffBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalOffBalanceGroup::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalTime::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalTrust::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Trust::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.InfoPc::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Mainer::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.MainBuh::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Executor::class.java)
    }

    private fun xstream() : XStream {

        val xstream = XStream(DomDriver("UTF-8"))

        //processAnnotation(xstream)

        xstream.useAttributeFor(String::class.java)
        xstream.useAttributeFor(Int::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.F101Xml::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Creator::class.java)
        xstream.alias("Составитель", Creator::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Balance::class.java)
        xstream.alias("Баланс", Balance::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Data101::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.OffBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.OffBalanceGroup::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.RowDataBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalOffBalance::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalOffBalanceGroup::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalTime::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.TotalTrust::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Trust::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.InfoPc::class.java)

        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Mainer::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.MainBuh::class.java)
        xstream.processAnnotations(ru.barabo.observer.config.cbr.f101.Executor::class.java)

        return xstream
    }

    fun load(file : File) :E {

        val fileInputStream = FileInputStream(file)

        @Suppress("UNCHECKED_CAST")
        val xml = xstream().fromXML(fileInputStream) as E

        fileInputStream.close()

        return xml
    }
}