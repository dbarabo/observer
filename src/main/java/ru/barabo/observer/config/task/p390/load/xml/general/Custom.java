package ru.barabo.observer.config.task.p390.load.xml.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("СведТО")
public class Custom {

    @XStreamAlias("КодТО")
    protected String code;

    @XStreamAlias("НаименТО")
    protected String name;

    @XStreamAlias("НаселПункт")
    protected String city;
}
