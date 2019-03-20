package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;

@XStreamAlias("СвПлОбоснов")
public class PayerJust {

    @XStreamAlias("НПлЮЛ")
    private String nameOrganization;

    @XStreamAlias("НПлИП")
    private Fio fioIp;

    @XStreamAlias("НПлФЛ")
    private Fio fioPhysic;
}
