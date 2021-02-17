package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("ПоВсем")
public class RestTypeAllAccounts {

    @XStreamAlias("ОстПоСост")
    private String onStateDate;

    @XStreamAlias("НаДату")
    private RestOnDate existAccountOnDate;

    @XStreamAlias("ВПериод")
    private RestOnDatePeriod existAccountOnPeriod;

    public Date getOnStateDate() {
        return XmlLoader.parseDate(onStateDate);
    }

    public Date getOpenAccountDate() {
        if(existAccountOnDate != null && existAccountOnDate.getOnDate() != null) {
            return existAccountOnDate.getOnDate();
        }

        return existAccountOnPeriod.getStartDate();
    }
}
