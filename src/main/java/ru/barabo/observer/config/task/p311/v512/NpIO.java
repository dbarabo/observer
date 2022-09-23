package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("НПИО")
public final class NpIO {

    @XStreamAlias("НаимОрг")
    private String name;

    @XStreamAlias("ИННИО")
    private InnIo innIo;//private String innIo;

    @XStreamAlias("КИО")
    private String kio;

    public NpIO(String name, String innIo, String kio) {

        this.kio = kio;
        this.innIo = new InnIo(innIo);
        this.name = name;
    }
}
