package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4")
public class DataForm310R4 {

    @XStreamAlias("Р4_1")
    final private String idCodeSubjectPledge;

    @XStreamAlias("Р4.1")
    final private SubSectionR41 subSectionR41;

    @XStreamAlias("Р4.2")
    final private SubSectionR42 subSectionR42;

    @XStreamAlias("Р4.3")
    final private SubSectionR43 subSectionR43;

    @XStreamAlias("Р4.4")
    final private SubSectionR44 subSectionR44;

    @XStreamAlias("Р4.5")
    final private SubSectionR45 subSectionR45;

    @XStreamAlias("Р4.6")
    final private SubSectionR46 subSectionR46;

    @XStreamAlias("Р4.7")
    final private SubSectionR47 subSectionR47;

    @XStreamAlias("Р4.8")
    final private SubSectionR48 subSectionR48;

    @XStreamAlias("Р4.9")
    final private SubSectionR49 subSectionR49;

    @XStreamAlias("Р4.10")
    final private SubSectionR410 subSectionR410;

    @XStreamAlias("Р4.11")
    final private SubSectionR411 subSectionR411;

    @XStreamAlias("Р4.12")
    final private SubSectionR412 subSectionR412;

    @XStreamAlias("Р4.13")
    final private SubSectionR413 subSectionR413;

    @XStreamAlias("Р4.14")
    final private SubSectionR414 subSectionR414;

    @XStreamAlias("Р4.15")
    final private SubSectionR415 subSectionR415;

    @XStreamAlias("Р4.16")
    final private SubSectionR416 subSectionR416;

    @XStreamAlias("Р4.17")
    final private SubSectionR417 subSectionR417;

    @XStreamAlias("Р4.18")
    final private SubSectionR418 subSectionR418;

    @XStreamAlias("Р4.19")
    final private SubSectionR419 subSectionR419;

    @XStreamAlias("Р4.20")
    final private SubSectionR420 subSectionR420;

    @XStreamAlias("Р4.20")
    final private SubSectionR421 subSectionR421;

    public DataForm310R4(Number idCodeSubjectPledge,
                         Number accountCode, Number codePledgedProperty, Number qualityCategory, Number idCodeGroup,
                         Integer collateralSign,
                         SubSectionR42 subSectionR42, SubSectionR43 subSectionR43, SubSectionR44 subSectionR44,
                         SubSectionR45 subSectionR45, SubSectionR46 subSectionR46, SubSectionR47 subSectionR47,
                         SubSectionR48 subSectionR48, SubSectionR49 subSectionR49, SubSectionR410 subSectionR410,
                         SubSectionR411 subSectionR411, SubSectionR412 subSectionR412, SubSectionR413 subSectionR413,
                         SubSectionR414 subSectionR414, SubSectionR415 subSectionR415, SubSectionR416 subSectionR416,
                         SubSectionR417 subSectionR417, SubSectionR418 subSectionR418, SubSectionR419 subSectionR419,
                         SubSectionR420 subSectionR420, SubSectionR421 subSectionR421) {

        this.idCodeSubjectPledge = idCodeSubjectPledge.toString();

        this.subSectionR41 = new SubSectionR41(accountCode, codePledgedProperty, qualityCategory, idCodeGroup, collateralSign);

        this.subSectionR42 = subSectionR42;

        this.subSectionR43 = subSectionR43;

        this.subSectionR44 = subSectionR44;

        this.subSectionR45 = subSectionR45;

        this.subSectionR46 = subSectionR46;

        this.subSectionR47 = subSectionR47;

        this.subSectionR48 = subSectionR48;

        this.subSectionR49 = subSectionR49;

        this.subSectionR410 = subSectionR410;

        this.subSectionR411 = subSectionR411;

        this.subSectionR412 = subSectionR412;

        this.subSectionR413 = subSectionR413;

        this.subSectionR414 = subSectionR414;

        this.subSectionR415 = subSectionR415;

        this.subSectionR416 = subSectionR416;

        this.subSectionR417 = subSectionR417;

        this.subSectionR418 = subSectionR418;

        this.subSectionR419 = subSectionR419;

        this.subSectionR420 = subSectionR420;

        this.subSectionR421 = subSectionR421;
    }
}
