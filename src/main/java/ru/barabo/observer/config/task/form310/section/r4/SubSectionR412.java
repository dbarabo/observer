package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.12")
public class SubSectionR412 {

    @XStreamAlias("Р4.12_2")
    final private String typeIntellectualProperty;

    @XStreamAlias("Р4.12_3")
    final private String documentNumber;

    public SubSectionR412(String typeIntellectualProperty, String documentNumber) {

        this.typeIntellectualProperty = typeIntellectualProperty;

        this.documentNumber = documentNumber;
    }
}
