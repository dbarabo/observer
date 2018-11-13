package ru.barabo.observer.config.cbr.ptkpsd.task.f101

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import java.io.File
import java.io.FileInputStream

abstract class AbstractXmlLoader<E> {

    protected abstract fun processAnnotation(xstream: XStream)

    private fun xstream() : XStream {

        val xstream = XStream(DomDriver())

        processAnnotation(xstream)

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