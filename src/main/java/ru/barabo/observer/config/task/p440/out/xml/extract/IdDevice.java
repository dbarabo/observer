package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ИдУстройства")
public class IdDevice {

    @XStreamAlias("IPАдр")
    final private String ipAddress;

    @XStreamAlias("МАСАдрес")
    final private String mac = "0";

    @XStreamAlias("НомSIM")
    final private String sim = "0";

    @XStreamAlias("НомТелеф")
    final private String phone = "0";

    @XStreamAlias("ИнойНомер")
    final private String other = "0";

    public IdDevice(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
