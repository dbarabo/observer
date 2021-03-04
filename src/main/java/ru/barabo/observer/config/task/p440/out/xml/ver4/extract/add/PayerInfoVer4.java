package ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("РеквПлат")
public class PayerInfoVer4 {

    @XStreamAlias("НаимПП")
    private String name;

    @XStreamAlias("ИННПП")
    private String inn;

    @XStreamAlias("КПППП")
    private String kpp;

    @XStreamAlias("НомСЧПП")
    private String account;

    public PayerInfoVer4(String name, String inn, String kpp, String account) {

        this.name = name;

        this.inn = inn;

        this.kpp = kpp;

        this.account = account;
    }
}
