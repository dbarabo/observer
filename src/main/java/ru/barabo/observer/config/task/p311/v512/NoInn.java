package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;

@XStreamAlias("НЕТИНН")
public final class NoInn {

    @XStreamAlias("НаимОрг")
    private String nameOrganization;

    @XStreamAlias("ФИОИП")
    private Fio fioIP;
}
