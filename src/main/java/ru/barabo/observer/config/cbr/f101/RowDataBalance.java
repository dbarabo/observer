package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class RowDataBalance {

    public String getAccount5() {
        return account5;
    }

    public String getCurrency() {
        return currency;
    }

    public String getActivePassive() {
        return activePassive;
    }

    public Integer getRestIn() { return restIn; }

    public Integer getTurnDebet() {
        return turnDebet;
    }

    public Integer getTurnCredit() {
        return turnCredit;
    }

    public Integer getRestOut() {
        return restOut;
    }

    @XStreamAlias("Счет2Пор")
    @XStreamAsAttribute
    private String account5;

    @XStreamAlias("Валюта")
    @XStreamAsAttribute
    private String currency;

    @XStreamAlias("ПризнакАП")
    @XStreamAsAttribute
    private String activePassive;

    @XStreamAlias("ВхОст")
    @XStreamAsAttribute
    private Integer restIn;

    @XStreamAlias("ОбДеб")
    @XStreamAsAttribute
    private Integer turnDebet;

    @XStreamAlias("ОбКр")
    @XStreamAsAttribute
    private Integer turnCredit;

    @XStreamAlias("ИсхОст")
    @XStreamAsAttribute
    private Integer restOut;
}
