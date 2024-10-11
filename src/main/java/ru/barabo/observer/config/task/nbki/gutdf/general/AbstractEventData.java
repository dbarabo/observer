package ru.barabo.observer.config.task.nbki.gutdf.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public abstract class AbstractEventData {

    @XStreamAlias("orderNum")
    final Integer orderNum;

    @XStreamAlias("eventDate")
    final public String eventDate;

    public AbstractEventData(Integer orderNum, Date eventDate) {

        this.orderNum = orderNum;

        this.eventDate = XmlLoader.formatDate(eventDate);
    }

    public String getEventDate() {
        return eventDate;
    }

    abstract public String getEvent();

    abstract public String getUnicalId();
}
