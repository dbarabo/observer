package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml;

@XStreamAlias("ВладСч")
public class RestOwnerAccount {

    @XStreamAlias("УказЛицо")
    private String subject;

    @XStreamAlias("ИноеЛицо")
    protected PayerXml anotherSubject;
}
