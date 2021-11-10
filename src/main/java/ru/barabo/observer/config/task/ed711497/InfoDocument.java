package ru.barabo.observer.config.task.ed711497;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("УдЛичнФЛ")
public class InfoDocument {

    @XStreamAlias("КодВидДок")
    final private String code;

    @XStreamAlias("СерНомДок")
    final private String lineNumber;

    @XStreamAlias("ДатаВыд")
    final private String dateOut;

    public InfoDocument(String code, String lineNumber, Date dateOut) {

        this.code = code;

        this.lineNumber = lineNumber;

        this.dateOut = XmlLoader.formatDate(dateOut);
    }
}
