package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("НаДату")
public class RestOnDate {

    @XStreamAlias("ДейстПоСост")
    private String onDate;

    public Date getOnDate() {
        return XmlLoader.parseDate(onDate);
    }
}
