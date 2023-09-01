package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.FioAttr;

@XStreamAlias("СвПлОбоснов")
public class PayerJust {

    @XStreamAlias("НПлЮЛ")
    private String nameOrganization;

    @XStreamAlias("НПлИП")
    private FioAttr fioIp;

    @XStreamAlias("НПлФЛ")
    private FioAttr fioPhysic;
}
