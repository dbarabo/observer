package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.10")
public class SubSectionR410 {

    @XStreamAlias("Р4.10_2")
    final private String agriculture;

    public  SubSectionR410(String agriculture) {

        this.agriculture = agriculture;
    }
}

