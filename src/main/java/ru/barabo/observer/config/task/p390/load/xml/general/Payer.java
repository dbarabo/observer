package ru.barabo.observer.config.task.p390.load.xml.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.Address;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

@XStreamAlias("������")
public class Payer {
    @XStreamAlias("������")
    protected PayerJur juric;

    @XStreamAlias("������")
    protected PayerPhysicIp physicIp;

    @XStreamAlias("���������")
    protected Address address;
}
