package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("ИмПериод")
public class DatePeriodOut {
    @XStreamAlias("НачПериода")
    final private String startDate;

    @XStreamAlias("ОконПериода")
    final private String endDate;

    public DatePeriodOut(Date start, Date end) {
        startDate = XmlLoader.formatDate(start);

        endDate = XmlLoader.formatDate(end);
    }

    public Date getStartDate() {

        return XmlLoader.parseDate(startDate);
    }

    public Date getEndDate() {

        return XmlLoader.parseDate(endDate);
    }
}
