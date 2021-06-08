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

    public SubSectionR41(Number accountCode, Number codePledgedProperty, Number qualityCategory, Number idCodeGroup,
                         Integer collateralSign) {

        this.accountCode = accountCode.toString();

        this.codePledgedProperty = codePledgedProperty.toString();

        this.qualityCategory = qualityCategory == null ? null : qualityCategory.toString();

        this.idCodeGroup = idCodeGroup == null ? null : idCodeGroup.toString();

        this.collateralSign = collateralSign.toString();
    }
}
