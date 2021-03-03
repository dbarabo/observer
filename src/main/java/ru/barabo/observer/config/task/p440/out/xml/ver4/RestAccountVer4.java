package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Счет")
public class RestAccountVer4 {

    @XStreamAlias("НомСч")
    private String code;

    @XStreamAlias("ДатаОткр")
    private String opened;

    @XStreamAlias("ДатаОкон")
    private String depositEnd;

    @XStreamAlias("ДатаЗакр")
    private String closed;

    @XStreamAlias("ВалютаСч")
    private CurrencyAccount currency;

    @XStreamAlias("ВидСч")
    private TypeAccountVer4 typeAccount;

    @XStreamAlias("СвОстат")
    private SvRestVer4 rest;

    public RestAccountVer4(String code, Date opened, Date closed, Date depositEnd, String currency,
                           String typeAccountCode, String typeAccountName, Number rest, Date restDate) {

        this.code = code;

        this.opened = XmlLoader.formatDate(opened);

        this.closed = XmlLoader.formatDate(closed);

        this.depositEnd = XmlLoader.formatDate(depositEnd);

        this.currency = new CurrencyAccount(currency);

        this.typeAccount = new TypeAccountVer4(typeAccountCode, typeAccountName);

        if(rest != null) {
            this.rest = new SvRestVer4(restDate, rest);
        }
    }
}
