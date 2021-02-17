package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("ПоУказанным")
public class RestTypeDetailAccounts {

    @XStreamAlias("ПорядкНомСч")
    private Integer orderAccount;

    @XStreamAlias("НомСч")
    private String accountCode;

    @XStreamAlias("ПоСост")
    private String onDate;

    @XStreamAlias("ВладСч")
    private RestOwnerAccount ownerAccount;

    public Date getOnDate() {
        return XmlLoader.parseDate(onDate);
    }

    public String getCode() {
        return accountCode;
    }

}
