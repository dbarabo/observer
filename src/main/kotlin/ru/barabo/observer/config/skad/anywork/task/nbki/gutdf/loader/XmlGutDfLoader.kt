package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.loader

import com.thoughtworks.xstream.XStream
import ru.barabo.observer.config.cbr.ptkpsd.task.f101.AbstractXmlLoader

class XmlGutDfLoader<E> : AbstractXmlLoader<E>() {

    override fun processAnnotation(xstream: XStream) {

        xstream.autodetectAnnotations(true)
    }
}