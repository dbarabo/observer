package ru.barabo.observer.config.task.p390.load.xml.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("����")
public class Bank {
    @XStreamAlias("���")
    protected String bik;

    @XStreamAlias("�����")
    protected String name;

    @XStreamAlias("����")
    protected String filial;
}
