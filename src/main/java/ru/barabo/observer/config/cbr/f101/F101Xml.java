package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Ф0409101")
public class F101Xml {

    @XStreamAlias("УникИдОЭС")
    @XStreamAsAttribute
    private String uid;

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

    @XStreamAlias("Пояснение")
    private String clarification;

    @XStreamAlias("ИнфПК")
    private InfoPc infoPc;

    @XStreamAlias("ПротоколКонтроля")
    @XStreamOmitField
    private Object protocolControl;

    public String getUid() {
        return uid;
    }

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

