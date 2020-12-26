package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("НПРО")
public final class NpRo {

    @XStreamAlias("ИННРО")
    private final String inn;

    @XStreamAlias("КПП")
    private final String kpp;

    @XStreamAlias("ОГРН")
    private final String ogrn;

    @XStreamAlias("НаимОрг")
    private final String name;

    public NpRo(String inn, String kpp, String ogrn, String name) {

        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.name = name;
    }
}
