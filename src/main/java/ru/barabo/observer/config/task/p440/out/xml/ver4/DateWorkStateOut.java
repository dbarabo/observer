package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("ДейстПоСост")
public class DateWorkStateOut {

    @XStreamAlias("Дата")
    final private String onDate;

    public DateWorkStateOut(Date onStateDate) {

        this.onDate = XmlLoader.formatDate(onStateDate);
    }
}
