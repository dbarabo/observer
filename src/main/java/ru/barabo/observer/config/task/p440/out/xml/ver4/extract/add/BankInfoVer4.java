package ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("РеквБанк")
public class BankInfoVer4 {
    @XStreamAlias("НомКорСЧ")
    private String corrAccount;

    @XStreamAlias("НаимБП")
    private String name;

    @XStreamAlias("КодБП")
    private BankBik bankBik;

    public BankInfoVer4(String corrAccount, String name, String bik) {

        this.corrAccount = corrAccount;

        this.name = name;

        this.bankBik = new BankBik(bik);
    }
}
