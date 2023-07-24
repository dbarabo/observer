package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ЮрЛицо")
public class ClientBankJuric {

    @XStreamAlias("ЮЛНаим")
    private String name;

    @XStreamAlias("ЮЛИНН")
    private String inn;

    @XStreamAlias("КППЮЛ")
    private String kpp;

    @XStreamAlias("ЮЛОГРН")
    private String ogrn;

    @XStreamAlias("ПризнакВнимания")
    private Integer isAttention;

    public String getName() {
        return name;
    }

    public String getInn() {
        return inn;
    }

    public String getKpp() {
        return kpp;
    }
}
