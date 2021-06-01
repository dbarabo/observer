package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.11")
public class SubSectionR411 {

    @XStreamAlias("Р4.11_2")
    final private String metalType;

    @XStreamAlias("Р4.11_3")
    final private String weightG;

    @XStreamAlias("Р4.11_4")
    final private String numberIngots;

    public  SubSectionR411(String metalType, String weightG, String numberIngots) {

        this.metalType = metalType;

        this.weightG = weightG;

        this.numberIngots = numberIngots;
    }
}
