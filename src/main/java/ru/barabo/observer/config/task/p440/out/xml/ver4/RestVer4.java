package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Остат")
public class RestVer4 {

    @XStreamAlias("ДатаОст")
    private String restDate;

    @XStreamAlias("Остаток")
    private String rest;

    public RestVer4(Date restDate, Number rest) {

        this.restDate = XmlLoader.formatDate(restDate);

        this.rest = XmlLoader.formatSum(rest);
    }
}
