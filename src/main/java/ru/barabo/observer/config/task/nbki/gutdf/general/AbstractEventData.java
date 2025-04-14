package ru.barabo.observer.config.task.nbki.gutdf.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public abstract class AbstractEventData {

    @XStreamAlias("orderNum")
    final Integer orderNum;

    @XStreamAlias("eventDate")
    final public String eventDate;

    transient private String eventValue;

    public AbstractEventData(Integer orderNum, Date eventDate, String eventValue) {

        this.eventValue = eventValue;

        this.orderNum = orderNum;

        this.eventDate = XmlLoader.formatDate(eventDate);
    }

    public String getEventDate() {
        return eventDate;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public String getEvent() {
        return eventValue;
    }

    public void setEvent(String eventValue) {
        this.eventValue = eventValue;
    }

    abstract public String getUnicalId();
}
