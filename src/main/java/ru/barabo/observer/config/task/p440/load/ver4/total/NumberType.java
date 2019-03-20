package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("НомерТип")
public class NumberType {

    @XStreamAlias("Тип")
    private String type;

    @XStreamAlias("Номер")
    private String number;
}
