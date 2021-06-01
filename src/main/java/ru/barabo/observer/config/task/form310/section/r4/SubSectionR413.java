package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("Р4.13")
public class SubSectionR413 {

    @XStreamAlias("Р4.13_2")
    final private String collateralAccountCode;

    @XStreamAlias("Р4.13_3")
    final private String infoAmountMoney;

    @XStreamAlias("Р4.13_4")
    final private String amountMoney;

    public SubSectionR413(String collateralAccountCode, String infoAmountMoney, Number amountMoney) {

        this.collateralAccountCode = collateralAccountCode;

        this.infoAmountMoney = infoAmountMoney;

        this.amountMoney = amountMoney == null ? null : XmlLoader.formatSum(amountMoney);
    }
}
