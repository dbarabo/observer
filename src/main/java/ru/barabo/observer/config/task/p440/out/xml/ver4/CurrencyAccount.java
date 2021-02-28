package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ВалютаСч")
public class CurrencyAccount {

    @XStreamAlias("КодВал")
    private String code;

    public CurrencyAccount(String code) {
        this.code = code;
    }
}
