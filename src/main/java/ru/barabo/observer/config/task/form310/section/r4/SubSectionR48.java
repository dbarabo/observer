package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.8")
public class SubSectionR48 {

    @XStreamAlias("Р4.8_2")
    final private String name;

    @XStreamAlias("Р4.8_3")
    final private String registrationNumber;

    @XStreamAlias("Р4.8_4")
    final private String yearIssue;

    public SubSectionR48(String name, String registrationNumber, Number yearIssue) {

        this.name = name;

        this.registrationNumber = registrationNumber;

        this.yearIssue = yearIssue == null ? null : yearIssue.toString();
    }
}
