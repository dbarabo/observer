package ru.barabo.observer.config.task.p390.load.xml.decision;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p390.load.xml.general.Bank;
import ru.barabo.observer.config.task.p390.load.xml.general.Custom;
import ru.barabo.observer.config.task.p390.load.xml.general.Payer;
import ru.barabo.observer.config.task.p440.load.xml.impl.Account;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;

import java.util.List;

@XStreamAlias("–ешение")
public class Decision {

    @XStreamAlias("Ќом–ешѕр")
    protected String numberDecisionApply;

    @XStreamAlias("ƒата–ешѕр")
    protected String dateDecisionApply; // "2018-09-03"

    @XStreamAlias("ќбсто€т")
    protected String mainDescription;

    @XStreamAlias("—ум¬зыск")
    private String mainSum;

    @XStreamAlias("Ќом“реб")
    private String numberClaim;

    @XStreamAlias("ƒата“реб")
    private String dateClaim;

    @XStreamAlias("Ќом–еш¬зыск")
    private String numberDecisionPenalty;

    @XStreamAlias("ƒата–еш¬зыск")
    private String dateDecisionPenalty;

    @XStreamAlias("—вед“ќ")
    private Custom custom;

    @XStreamAlias("‘»ќ–ук")
    private Fio fioHead;

    @XStreamAlias("Ѕанк")
    private Bank bank;

    @XStreamAlias("—ведѕл")
    private Payer payer;

    @XStreamImplicit(itemFieldName = "—чет")
    private List<Account> accounts;
}
