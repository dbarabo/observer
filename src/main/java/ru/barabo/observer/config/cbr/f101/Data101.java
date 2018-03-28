package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Данные101")
public class Data101 {

    @XStreamAlias("Баланс")
    private Balance balance;

    @XStreamAlias("ИтогоБаланс")
    private TotalBalance totalBalance;

    @XStreamAlias("ИтогоДоверит")
    private TotalTrust totalTrust;

    @XStreamAlias("Внебал")
    private OffBalanceGroup offBalanceGroup;

    @XStreamAlias("ИтогВнебал")
    private TotalOffBalanceGroup totalOffBalanceGroup;

    @XStreamAlias("ИтогоСрочные")
    private TotalTime totalTime;

    public Balance getBalance() {
        return balance;
    }

    public TotalBalance getTotalBalance() {
        return totalBalance;
    }

    public TotalTrust getTotalTrust() {
        return totalTrust;
    }

    public OffBalanceGroup getOffBalanceGroup() {
        return offBalanceGroup;
    }

    public TotalOffBalanceGroup getTotalOffBalanceGroup() {
        return totalOffBalanceGroup;
    }

    public TotalTime getTotalTime() {
        return totalTime;
    }
}
