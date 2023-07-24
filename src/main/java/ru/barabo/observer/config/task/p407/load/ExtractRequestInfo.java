package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ЭЛ04ЗапрВыписок")
public class ExtractRequestInfo {

    @XStreamAlias("ВидИнфКод")
    private String code;

    @XStreamAlias("ВидИнфНаименование")
    private String name;

    @XStreamAlias("ДатаН")
    private String dateStart;

    @XStreamAlias("ДатаК")
    private String dateEnd;

    @XStreamAlias("КодСчета")
    private String accountTypes;

    @XStreamAlias("КодБКарта")
    private String cardTypes;

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }
}
