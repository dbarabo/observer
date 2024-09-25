package ru.barabo.observer.config.task.nbki.gutdf.general;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public class AbstractEventData {

    @XStreamAlias("orderNum")
    final Integer orderNum;

    @XStreamAlias("EventDate")
    final String eventDate;

    public AbstractEventData(Integer orderNum, Date eventDate) {

        this.orderNum = orderNum;

        this.eventDate = XmlLoader.formatDate(eventDate);
    }
}