package ru.barabo.observer.config.task.p390.load.xml.decision;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p390.load.xml.general.Bank;
import ru.barabo.observer.config.task.p390.load.xml.general.Custom;
import ru.barabo.observer.config.task.p390.load.xml.general.Payer;
import ru.barabo.observer.config.task.p440.load.xml.impl.Account;
import ru.barabo.observer.config.task.p440.load.xml.impl.FioAttr;

import java.util.List;

@XStreamAlias("Решение")
public class Decision {

    @XStreamAlias("НомРешПр")
    protected String numberDecisionApply;

    @XStreamAlias("ДатаРешПр")
    protected String dateDecisionApply; // "2018-09-03"

    @XStreamAlias("Обстоят")
    protected String mainDescription;

    @XStreamAlias("СумВзыск")
    private String mainSum;

    @XStreamAlias("НомТреб")
    private String numberClaim;

    @XStreamAlias("ДатаТреб")
    private String dateClaim;

    @XStreamAlias("НомРешВзыск")
    private String numberDecisionPenalty;

    @XStreamAlias("ДатаРешВзыск")
    private String dateDecisionPenalty;

    @XStreamAlias("СведТО")
    private Custom custom;

    @XStreamAlias("ФИОРук")
    private FioAttr fioHead;

    @XStreamAlias("Банк")
    private Bank bank;

    @XStreamAlias("СведПл")
    private Payer payer;

    @XStreamImplicit(itemFieldName = "Счет")
    private List<Account> accounts;
}
