package ru.barabo.observer.config.task.p390.load.xml.decision;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p390.load.xml.general.Bank;
import ru.barabo.observer.config.task.p390.load.xml.general.Custom;
import ru.barabo.observer.config.task.p390.load.xml.general.Payer;
import ru.barabo.observer.config.task.p440.load.xml.impl.Account;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;

import java.util.List;

@XStreamAlias("�������")
public class Decision {

    @XStreamAlias("��������")
    protected String numberDecisionApply;

    @XStreamAlias("���������")
    protected String dateDecisionApply; // "2018-09-03"

    @XStreamAlias("�������")
    protected String mainDescription;

    @XStreamAlias("��������")
    private String mainSum;

    @XStreamAlias("�������")
    private String numberClaim;

    @XStreamAlias("��������")
    private String dateClaim;

    @XStreamAlias("�����������")
    private String numberDecisionPenalty;

    @XStreamAlias("������������")
    private String dateDecisionPenalty;

    @XStreamAlias("������")
    private Custom custom;

    @XStreamAlias("������")
    private Fio fioHead;

    @XStreamAlias("����")
    private Bank bank;

    @XStreamAlias("������")
    private Payer payer;

    @XStreamImplicit(itemFieldName = "����")
    private List<Account> accounts;
}
