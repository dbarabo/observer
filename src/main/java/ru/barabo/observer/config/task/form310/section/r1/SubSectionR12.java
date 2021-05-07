package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р1.2")
public class SubSectionR12 {

    @XStreamAlias("Р1.2_2")
    final private String idPledger;

    public SubSectionR12(String idPledger) {

        this.idPledger = idPledger;
    }
}
