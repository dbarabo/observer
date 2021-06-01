package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.17")
public class SubSectionR417 {

    @XStreamAlias("Р4.17_2")
    final private String countUnitsMutualInvestmentFund;

    @XStreamAlias("Р4.17_3")
    final private String codeIsin;

    @XStreamAlias("Р4.17_4")
    final private String registrationNumber;

    public SubSectionR417(Number countUnitsMutualInvestmentFund, String codeIsin, String registrationNumber) {

        this.countUnitsMutualInvestmentFund = countUnitsMutualInvestmentFund.toString();

        this.codeIsin = codeIsin;

        this.registrationNumber = registrationNumber;
    }
}
