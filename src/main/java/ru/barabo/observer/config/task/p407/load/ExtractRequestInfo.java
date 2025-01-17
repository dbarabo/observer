package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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

    @XStreamAlias("КомЗапр")
    @XStreamOmitField
    private String comment;

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }
}
