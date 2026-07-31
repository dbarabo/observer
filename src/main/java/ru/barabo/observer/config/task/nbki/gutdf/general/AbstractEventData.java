package ru.barabo.observer.config.task.nbki.gutdf.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Timestamp;
import java.util.Date;

public abstract class AbstractEventData {

    @XStreamAlias("orderNum")
    final Integer orderNum;

    @XStreamAlias("eventDate")
    final private String eventDate;

    @XStreamAlias("eventDateTime")
    final public String eventDateTime;

    transient private String eventValue;


    public AbstractEventData(Integer orderNum, Date eventDate, String eventValue) {

        this.eventValue = eventValue;

        this.orderNum = orderNum;

        this.eventDate = XmlLoader.isNewFormatGutdf() ? null : XmlLoader.formatDate(eventDate);

        this.eventDateTime = XmlLoader.isNewFormatGutdf() ? XmlLoader.formatDateTimeWithZone10(eventDate) : null;
    }

    public String getEventDate() {

        return XmlLoader.isNewFormatGutdf() ? eventDateTime.substring(0, "yyyy-mm-dd".length()) : eventDate;
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
