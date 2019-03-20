package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Обоснов")
public class Justification {

    @XStreamAlias("КодОбоснов")
    private String codeJustification;

    @XStreamAlias("ВидПров")
    private String kindTaxCheck;

    @XStreamAlias("НаимНО")
    private String nameFns;

    @XStreamAlias("КодНО")
    private String codeFns;

    @XStreamAlias("ДолжнЛицСогл")
    private String officerAgree;

    @XStreamAlias("ФИОЛицСогл")
    private String fioOfficerAgree;

    @XStreamAlias("ВидДокСогл")
    private String kindDocAgree;

    @XStreamAlias("ДатаДокСогл")
    private String dateDocAgree;

    @XStreamAlias("НомерДокСогл")
    private String numberDocAgree;

    @XStreamAlias("ВидСделки")
    private String kindDeal;

    @XStreamAlias("ТипЛица")
    private String typeOrganization;

    @XStreamAlias("ВидРешПр")
    private String kindDecision;


    @XStreamAlias("СвПлОбоснов")
    private PayerJust payerJust;


}
