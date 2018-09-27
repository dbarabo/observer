package ru.barabo.observer.config.task.p390.load.xml.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.Address;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

@XStreamAlias("СведПл")
public class Payer {
    @XStreamAlias("ПлатЮЛ")
    protected PayerJur juric;

    @XStreamAlias("ПлатФЛ")
    protected PayerPhysicIp physicIp;

    @XStreamAlias("АдресПлат")
    protected Address address;
}
