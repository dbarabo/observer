package ru.barabo.observer.config.skad.anywork.task.cbr.extract.loader

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission
import ru.barabo.observer.config.cbr.ptkpsd.task.f101.AbstractXmlLoader
import ru.barabo.observer.config.task.cbr.extract.request.MainFileRequest
import ru.barabo.observer.config.task.cbr.extract.request.RequestVko
import ru.barabo.observer.config.task.cbr.extract.request.RequestVkoListIdRequest
import ru.barabo.observer.config.task.cbr.extract.request.RequestVkoPeriodList

class XmlCbrRequestLoader<E> : AbstractXmlLoader<E>() {

    override fun processAnnotation(xstream: XStream) {
        xstream.addPermission(AnyTypePermission.ANY)

        xstream.autodetectAnnotations(true)

        xstream.processAnnotations(MainFileRequest::class.java)
        xstream.processAnnotations(RequestVko::class.java)
        xstream.processAnnotations(RequestVkoListIdRequest::class.java)
        xstream.processAnnotations(RequestVkoPeriodList::class.java)

        xstream.useAttributeFor(String::class.java)
        xstream.useAttributeFor(Int::class.java)
        xstream.useAttributeFor(Integer::class.java)
    }

}