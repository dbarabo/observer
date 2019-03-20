package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ВидНаимКодТип")
public class AddressKindTypeName {

    @XStreamAlias("ВидКод")
    private String uin;

    @XStreamAlias("Наим")
    private String name;
}
