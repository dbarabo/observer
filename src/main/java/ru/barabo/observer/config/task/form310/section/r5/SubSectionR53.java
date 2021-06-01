package ru.barabo.observer.config.task.form310.section.r5;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р5.3")
public class SubSectionR53 {
    @XStreamAlias("Р5.3_2")
    private final String codeCountryOksm;

    @XStreamAlias("Р5.3_3")
    private final String fullName;

    @XStreamAlias("Р5.3_4")
    private final String registrationNumberBank;

    @XStreamAlias("Р5.3_5")
    private final String bik;

    @XStreamAlias("Р5.3_6")
    private final String swift;

    @XStreamAlias("Р5.3_7")
    private final String tin;

    @XStreamAlias("Р5.3_8")
    private final String lei;

    @XStreamAlias("Р5.3_9")
    private final String registrationNumberCountry;

    public SubSectionR53(Number codeCountryOksm, String fullName, String registrationNumberBank, String bik, String swift,
                         String tin, String lei, String registrationNumberCountry) {

        this.codeCountryOksm = codeCountryOksm.toString();

        this.fullName = fullName;

        this.registrationNumberBank = registrationNumberBank;

        this.bik = bik;

        this.swift = swift;

        this.tin = tin;

        this.lei = lei;

        this.registrationNumberCountry = registrationNumberCountry;
    }
}
