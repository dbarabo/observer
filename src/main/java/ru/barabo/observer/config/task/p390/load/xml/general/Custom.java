package ru.barabo.observer.config.task.p390.load.xml.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("������")
public class Custom {

    @XStreamAlias("�����")
    protected String code;

    @XStreamAlias("��������")
    protected String name;

    @XStreamAlias("����������")
    protected String city;
}
