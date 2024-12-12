package ru.barabo.observer.config.task.cbr.extract.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class RequestVkoPeriodList {

    @XStreamAlias("ДатаНачПериодЗначСпис")
    public String startPeriod;

    @XStreamAlias("ДатаОконПериодЗначСпис")
    public String endPeriod;
}
