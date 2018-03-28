package ru.barabo.observer.config.cbr.ptkpsd.task.p550

import com.thoughtworks.xstream.XStream
import ru.barabo.observer.config.cbr.ptkpsd.task.f101.AbstractXmlLoader
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.load.*


class XmlLoader<E> : AbstractXmlLoader<E>() {

    override fun processAnnotation(xstream: XStream) {

        xstream.processAnnotations(CbEs550pXml::class.java)
        xstream.processAnnotations(ServicePart::class.java)
        xstream.processAnnotations(InfoPart550::class.java)
        xstream.processAnnotations(Part11::class.java)
        xstream.processAnnotations(Part12::class.java)
        xstream.processAnnotations(Part2::class.java)
        xstream.processAnnotations(Part550::class.java)
    }
}