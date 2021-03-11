package ru.barabo.observer.config.task.p440.out.xml.ver4.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.out.xml.ver4.TypeAccountVer4;

import java.util.Date;

@XStreamAlias("Счет")
public class ExistsAccountVer4 {
    @XStreamAlias("НомСч")
    private String code;

    @XStreamAlias("ДатаОткр")
    private String opened;

    @XStreamAlias("ДатаОкон")
    private String depositEnd;

    @XStreamAlias("ДатаЗакр")
    private String closed;

    @XStreamAlias("КодВал")
    private String currencyCode;

    @XStreamAlias("ВидСч")
    private TypeAccountVer4 typeAccount;

    public ExistsAccountVer4(String code, Date opened, Date closed, Date depositEnd, String currency,
                           String typeAccountCode, String typeAccountName) {

        this.code = code;

        this.opened = XmlLoader.formatDate(opened);

        this.closed = XmlLoader.formatDate(closed);

        this.depositEnd = XmlLoader.formatDate(depositEnd);

        this.currencyCode = currency;

        this.typeAccount = new TypeAccountVer4(typeAccountCode, typeAccountName);
    }
}
