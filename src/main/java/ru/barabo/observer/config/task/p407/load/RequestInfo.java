package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("ВидЗапрИнфо")
public class RequestInfo {

    @XStreamAlias("ЭЛ04ЗапрВыписок")
    private ExtractRequestInfo extractRequestInfo;

    @XStreamAlias("ЭЛ01ЗапрКопДог")
    @XStreamOmitField
    private Object copyPact;

    @XStreamAlias("ЭЛ02ЗапрВБК")
    @XStreamOmitField
    private Object vbkPact;

    @XStreamAlias("ЭЛ03ЗапрПодп")
    @XStreamOmitField
    private Object signPact;

    @XStreamAlias("ЭЛ08ЗапрЗаявлНаОткрСчета")
    @XStreamOmitField
    private Object appPact;

    @XStreamAlias("ЭЛ09ЗапросДосьеКлиента")
    @XStreamOmitField
    private Object clientPact;

    @XStreamAlias("ЭЛ91ЗапрБенефиц")
    @XStreamOmitField
    private Object benPact;

    @XStreamAlias("ЭЛ10ЗапрКопийПаспортов")
    @XStreamOmitField
    private Object pasportPact;

    public ExtractRequestInfo getExtractRequestInfo() {
        return extractRequestInfo;
    }
}
