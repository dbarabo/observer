package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.1")
public class SubSectionR41 {

    @XStreamAlias("Р4.1_2")
    final private String accountCode;

    @XStreamAlias("Р4.1_3")
    final private String codePledgedProperty;

    @XStreamAlias("Р4.1_4")
    final private String qualityCategory;

    @XStreamAlias("Р4.1_5")
    final private String idCodeGroup;

    @XStreamAlias("Р4.1_6")
    final private String collateralSign;

    public SubSectionR41(String accountCode, String codePledgedProperty, String qualityCategory, Number idCodeGroup, Number collateralSign) {

        this.accountCode = accountCode;

        this.codePledgedProperty = codePledgedProperty;

        this.qualityCategory = qualityCategory;

        this.idCodeGroup = idCodeGroup == null ? null : idCodeGroup.toString();

        this.collateralSign = collateralSign.toString();
    }
}
