package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ТипНаимТип")
public class ElementPlanStruct {

    @XStreamAlias("Тип")
    private String type;

    @XStreamAlias("Наим")
    private String name;
}
