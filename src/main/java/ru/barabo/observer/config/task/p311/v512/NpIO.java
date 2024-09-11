package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("НПИО")
public final class NpIO {

    @XStreamAlias("НаимОрг")
    private String name;

    @XStreamAlias("ИННИО")
    private InnIo innIo;//private String innIo;

    @XStreamAlias("КИО")
    private KioIo kio;

    public NpIO(String name, String innIo, String kio) {

        this.kio = ((kio == null) || kio.isEmpty()) ? null : new KioIo(kio);

        this.innIo = ((innIo == null) || innIo.isEmpty()) ? null : new InnIo(innIo);

        this.name = name;
    }
}
