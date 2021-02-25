package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("ПоВсем")
public class TypeAllAccounts {

    @XStreamAlias("ОстПоСост")
    private String onStateDate;

    @XStreamAlias("ДатаНач")
    private String startDate;

    @XStreamAlias("ДатаКон")
    private String endDate;

    @XStreamAlias("НаДату")
    private RestOnDate existAccountOnDate;

    @XStreamAlias("ВПериод")
    private RestOnDatePeriod existAccountOnPeriod;

    public Date getOnStateDate() {
        return XmlLoader.parseDate(onStateDate);
    }

    public Date getStartDate() {
        return XmlLoader.parseDate(startDate);
    }

    public Date getEndDate() {
        return XmlLoader.parseDate(endDate);
    }

    public Date getOpenAccountDate() {
        if(existAccountOnDate != null && existAccountOnDate.getOnDate() != null) {
            return existAccountOnDate.getOnDate();
        }

        return existAccountOnPeriod.getStartDate();
    }
}
