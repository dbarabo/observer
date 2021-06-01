package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.7")
public class SubSectionR47 {

    @XStreamAlias("Р4.7_2")
    final private String factoryNumber;

    @XStreamAlias("Р4.7_3")
    final private String yearIssue;

    @XStreamAlias("Р4.7_4")
    final private String typeUnitRailway;

    @XStreamAlias("Р4.7_5")
    final private String modelRailway;

    @XStreamAlias("Р4.7_6")
    final private String carriageNumber;

    public SubSectionR47(String factoryNumber, Number yearIssue, String typeUnitRailway, String modelRailway, String carriageNumber) {

        this.factoryNumber = factoryNumber;

        this.yearIssue = yearIssue == null ? null : yearIssue.toString();

        this.typeUnitRailway = typeUnitRailway;

        this.modelRailway = modelRailway;

        this.carriageNumber = carriageNumber;
    }
}
