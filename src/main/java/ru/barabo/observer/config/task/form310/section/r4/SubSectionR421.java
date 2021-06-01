package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.21")
public class SubSectionR421 {

    @XStreamAlias("Р4.21_2")
    final private String type;

    @XStreamAlias("Р4.21_3")
    final private String name;

    public SubSectionR421(String type, String name) {

        this.type = type;

        this.name = name;
    }
}
