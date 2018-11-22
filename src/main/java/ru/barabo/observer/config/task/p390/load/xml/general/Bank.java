package ru.barabo.observer.config.task.p390.load.xml.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Банк")
public class Bank {
    @XStreamAlias("БИК")
    protected String bik;

    @XStreamAlias("НаимБ")
    protected String name;

    @XStreamAlias("НомФ")
    protected String filial;
}
