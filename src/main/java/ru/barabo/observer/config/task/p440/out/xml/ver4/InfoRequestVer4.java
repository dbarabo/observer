package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("СведЗапр")
public class InfoRequestVer4 {

    @XStreamAlias("НомЗапр")
    private String numberRequest;

    @XStreamAlias("ДатаЗапр")
    private String dateRequest;

    @XStreamAlias("КодНО")
    private String fnsCode;

    @XStreamAlias("ИндЗапр")
    private String idRequestFns;
}
