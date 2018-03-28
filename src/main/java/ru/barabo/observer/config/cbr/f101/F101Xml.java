package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Ф0409101")
public class F101Xml {

    @XStreamAlias("ОтчДата")
    @XStreamAsAttribute
    private String reportDate;

    @XStreamAlias("Периодичность")
    @XStreamAsAttribute
    private String typeReport;

    @XStreamAlias("ДатаВремяФормирования")
    @XStreamAsAttribute
    private String createDateTime;

    @XStreamAlias("Составитель")
    private Creator creator;

    @XStreamAlias("Данные101")
    private Data101 data101;

    @XStreamAlias("ИнфПК")
    private InfoPc infoPc;

    public String getReportDate() {
        return reportDate;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public Creator getCreator() {
        return creator;
    }

    public Data101 getData101() {
        return data101;
    }

    public InfoPc getInfoPc() {
        return infoPc;
    }
}

