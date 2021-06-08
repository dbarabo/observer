package ru.barabo.observer.config.task.form310.section.r5;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р5")
public class DataForm310R5 {

    @XStreamAlias("Р5_1")
    final private String idCodeSubject;

    @XStreamAlias("Р5.1")
    final private SubSectionR51 subSectionR51;

    @XStreamAlias("Р5.2")
    final private SubSectionR52 subSectionR52;

    @XStreamAlias("Р5.3")
    final private SubSectionR53 subSectionR53;

    public DataForm310R5(Number idCodeSubject, SubSectionR51 subSectionR51) {

        this.idCodeSubject = idCodeSubject.toString();

        this.subSectionR51 = subSectionR51;

        this.subSectionR52 = null;

        this.subSectionR53 = null;
    }

    public DataForm310R5(Number idCodeSubject, SubSectionR52 subSectionR52) {

        this.idCodeSubject = idCodeSubject.toString();

        this.subSectionR51 = null;

        this.subSectionR52 = subSectionR52;

        this.subSectionR53 = null;
    }

    public DataForm310R5(Number idCodeSubject, SubSectionR53 subSectionR53) {

        this.idCodeSubject = idCodeSubject.toString();

        this.subSectionR51 = null;

        this.subSectionR52 = null;

        this.subSectionR53 = subSectionR53;
    }
}
