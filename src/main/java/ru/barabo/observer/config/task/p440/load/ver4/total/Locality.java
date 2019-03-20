package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ВидНаимТип")
public class Locality {

    @XStreamAlias("Вид")
    private String kind;

    @XStreamAlias("Наим")
    private String name;
}
