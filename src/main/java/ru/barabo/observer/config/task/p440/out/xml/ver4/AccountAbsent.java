package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("СчетОтсут")
public class AccountAbsent {

    @XStreamAlias("НомСч")
    private String codeAccount;

    public AccountAbsent(String codeAccount) {
        this.codeAccount = codeAccount;
    }
}
