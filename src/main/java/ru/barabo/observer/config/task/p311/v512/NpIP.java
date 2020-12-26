package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;

@XStreamAlias("НПИП")
public final class NpIP {
    @XStreamAlias("ИННИП")
    private String inn;

    @XStreamAlias("ОГРНИП")
    private String ogrn;

    @XStreamAlias("ФИОИП")
    private Fio fio;

    public NpIP(String inn, String ogrn, String firstName, String lastName, String secondName) {

        this.inn = inn;
        this.ogrn = ogrn;
        this.fio = new Fio(firstName, lastName, secondName);
    }
}
