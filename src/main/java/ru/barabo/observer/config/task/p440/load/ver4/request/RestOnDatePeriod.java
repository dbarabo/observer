package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("ВПериод")
public class RestOnDatePeriod {

    @XStreamAlias("ДатаНач")
    private String startDate;

    public Date getStartDate() {
        return XmlLoader.parseDate(startDate);
    }

    @XStreamAlias("ДатаКон")
    private String endDate;

    public Date getEndDate() {
        return XmlLoader.parseDate(endDate);
    }
}
