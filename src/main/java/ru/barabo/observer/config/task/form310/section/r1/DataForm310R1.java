package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Date;

@XStreamAlias("Р1")
public class DataForm310R1 {

    @XStreamAlias("Р1_1")
    final private String idPactPledge;

    @XStreamAlias("РР1")
    final private SubSectionR1123 subSectionR1123;

    @XStreamAlias("Р1.4")
    final private SubSectionR14 subSectionR14;

    public DataForm310R1(String idPactPledge, SubSectionR1123 subSectionR1123) {

        this.idPactPledge = idPactPledge;

        this.subSectionR1123 = subSectionR1123;

        this.subSectionR14 = null;
    }

    public DataForm310R1(String idPactPledge, Date closePledge) {

        this.idPactPledge = idPactPledge;

        this.subSectionR14 = new SubSectionR14(closePledge);

        this.subSectionR1123 = null;
    }
}
