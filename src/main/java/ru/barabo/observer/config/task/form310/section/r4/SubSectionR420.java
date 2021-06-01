package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.20")
public class SubSectionR420 {

    @XStreamAlias("Р4.20_2")
    final private String typePropertyRights;

    @XStreamAlias("Р4.20_3")
    final private String name;

    public SubSectionR420(String typePropertyRights, String name) {

        this.typePropertyRights = typePropertyRights;

        this.name = name;
    }
}
