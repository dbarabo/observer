package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.5")
public class SubSectionR45 {

    @XStreamAlias("Р4.5_2")
    final private String aircraftType;

    @XStreamAlias("Р4.5_3")
    final private String serialNumber;

    @XStreamAlias("Р4.5_4")
    final private String gliderNumber;

    @XStreamAlias("Р4.5_5")
    final private String yearIssue;

    @XStreamAlias("Р4.5_6")
    final private String manufacturerName;

    @XStreamAlias("Р4.5_7")
    final private String typeName;

    public SubSectionR45(String aircraftType, String serialNumber, String gliderNumber, Number yearIssue,
                         String manufacturerName, String typeName) {
        this.aircraftType = aircraftType;

        this.serialNumber = serialNumber;

        this.gliderNumber = gliderNumber;

        this.yearIssue = yearIssue == null ? null : yearIssue.toString();

        this.manufacturerName = manufacturerName;

        this.typeName = typeName;
   }
}
