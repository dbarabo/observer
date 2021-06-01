package ru.barabo.observer.config.task.form310.section.r2;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Р2")
public class DataForm310R2 {

    @XStreamAlias("Р2_1")
    final private String idCodeSubjectPledge;

    @XStreamAlias("Р2_2")
    final private String idCodeGroup;

    @XStreamAlias("Р2_3")
    final private String amountSubject;

    @XStreamAlias("Р2_4")
    final private String dateDefineAmountSubject;

    @XStreamAlias("Р2_5")
    final private String amountMarket;

    @XStreamAlias("Р2_6")
    final private String dateDefineAmountMarket;

    @XStreamAlias("Р2_7")
    final private String amountCadastral;

    @XStreamAlias("Р2_8")
    final private String dateDefineAmountCadastral;

    @XStreamAlias("Р2_9")
    final private String amountLiquidation;

    @XStreamAlias("Р2_10")
    final private String dateDefineAmountLiquidation;

    @XStreamAlias("Р2_11")
    final private String amountInvestment;

    @XStreamAlias("Р2_12")
    final private String dateDefineAmountInvestment;

    @XStreamAlias("Р2_13")
    final private String costAgreed;

    @XStreamAlias("Р2_14")
    final private String dateDefineCostAgreed;

    public DataForm310R2(Number idCodeSubjectPledge, Number idCodeGroup,
                         Number amountSubject, Date dateDefineAmountSubject,
                         Number amountMarket, Date dateDefineAmountMarket,
                         Number amountCadastral, Date dateDefineAmountCadastral,
                         Number amountLiquidation, Date dateDefineAmountLiquidation,
                         Number amountInvestment, Date dateDefineAmountInvestment,
                         Number costAgreed, Date dateDefineCostAgreed) {

        this.idCodeSubjectPledge = idCodeSubjectPledge == null ? null : idCodeSubjectPledge.toString();

        this.idCodeGroup = (idCodeGroup == null) ? null : idCodeGroup.toString();

        this.amountSubject = XmlLoader.formatSum(amountSubject);
        this.dateDefineAmountSubject = XmlLoader.formatDate( dateDefineAmountSubject );

        this.amountMarket = XmlLoader.formatSum(amountMarket);
        this.dateDefineAmountMarket = XmlLoader.formatDate( dateDefineAmountMarket );

        this.amountCadastral = XmlLoader.formatSum(amountCadastral);
        this.dateDefineAmountCadastral = XmlLoader.formatDate( dateDefineAmountCadastral );

        this.amountLiquidation = XmlLoader.formatSum(amountLiquidation);
        this.dateDefineAmountLiquidation = XmlLoader.formatDate( dateDefineAmountLiquidation );

        this.amountInvestment = XmlLoader.formatSum(amountInvestment);
        this.dateDefineAmountInvestment = XmlLoader.formatDate( dateDefineAmountInvestment );

        this.costAgreed = XmlLoader.formatSum(costAgreed);
        this.dateDefineCostAgreed = XmlLoader.formatDate( dateDefineCostAgreed );
    }
}
