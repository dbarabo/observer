package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.6")
public class SubSectionR46 {

    @XStreamAlias("Р4.6_2")
    final private String group;

    @XStreamAlias("Р4.6_3")
    final private String numberImo;

    @XStreamAlias("Р4.6_4")
    final private String numberMmsi;

    @XStreamAlias("Р4.6_5")
    final private String shipClass;

    @XStreamAlias("Р4.6_6")
    final private String registerNumber;

    @XStreamAlias("Р4.6_7")
    final private String yearIssue;

    @XStreamAlias("Р4.6_8")
    final private String name;

    public SubSectionR46(String group, String numberImo, String numberMmsi, String shipClass, String registerNumber,
                         Number yearIssue, String name) {

        this.group = group;

        this.numberImo = numberImo;

        this.numberMmsi = numberMmsi;

        this.shipClass = shipClass;

        this.registerNumber = registerNumber;

        this.yearIssue = yearIssue == null ? null : yearIssue.toString();

        this.name = name;
    }
}
