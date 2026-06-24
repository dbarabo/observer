package ru.barabo.observer.config.task.fz263.load.xml.upv;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("Поручения")
public class OrderUno {

    @XStreamAlias("НомПорИсп")
    private String numberUno;

    @XStreamAlias("ДатаПорИсп")
    private String dateUno;

    public Date getDateUno() {
        return XmlLoader.parseDate(dateUno);
    }

    public String getNumberUno() {
        return numberUno;
    }
}
