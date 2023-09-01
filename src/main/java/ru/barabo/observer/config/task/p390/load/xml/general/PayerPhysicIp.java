package ru.barabo.observer.config.task.p390.load.xml.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.FioAttr;

@XStreamAlias("ПлатФЛ")
public class PayerPhysicIp {

    @XStreamAlias("ИННФЛ")
    protected String inn;

    @XStreamAlias("ФИОФЛ")
    protected FioAttr fio;
}
