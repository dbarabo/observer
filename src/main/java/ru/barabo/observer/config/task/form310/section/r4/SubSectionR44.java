package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.4")
public class SubSectionR44 {

    @XStreamAlias("Р4.4_2")
    final private String name;

    @XStreamAlias("Р4.4_3")
    final private String factoryNumber;

    @XStreamAlias("Р4.4_4")
    final private String inventoryNumber;

    @XStreamAlias("Р4.4_5")
    final private String yearIssue;

    @XStreamAlias("Р4.4_6")
    final private String brand;

    @XStreamAlias("Р4.4_7")
    final private String model;

    @XStreamAlias("Р4.4_8")
    final private String group;

    public SubSectionR44(String name, String factoryNumber, String inventoryNumber, Number yearIssue, String brand,
                         String model, String group) {

        this.name = name;

        this.factoryNumber = factoryNumber;

        this.inventoryNumber = inventoryNumber;

        this.yearIssue = yearIssue == null ? null : yearIssue.toString();

        this.brand = brand;

        this.model = model;

        this.group = group;
    }
}
