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

    @XStreamAlias("ЭЛ05ЗапрОперБезОткрСчета")
    @XStreamOmitField
    private Object operNoAccount;

    @XStreamAlias("ЭЛ06ЗапрОперЭлДенСредств")
    @XStreamOmitField
    private Object operElectr;

    @XStreamAlias("ЭЛ07ЗапрВалютнОперБезОткрСчета")
    @XStreamOmitField
    private Object operWithoutAccount;

    @XStreamAlias("ЭЛ08ЗапрЗаявлНаОткрСчета")
    @XStreamOmitField
    private Object appPact;

    @XStreamAlias("ЭЛ09ЗапросДосьеКлиента")
    @XStreamOmitField
    private Object clientPact;

    @XStreamAlias("ЭЛ10ЗапрКопийПаспортов")
    @XStreamOmitField
    private Object pasportPact;

    @XStreamAlias("ЭЛ11ЗапрОперСВекселями")
    @XStreamOmitField
    private Object operVeksel;

    @XStreamAlias("ЭЛ13ЗапрСберДепСерт")
    @XStreamOmitField
    private Object operdepoz;

    @XStreamAlias("ЭЛ91ЗапрБенефиц")
    @XStreamOmitField
    private Object benPact;

    @XStreamAlias("ЭЛ14ЗапрРасшВыписок")
    @XStreamOmitField
    private Object operExtExtract;

    public ExtractRequestInfo getExtractRequestInfo() {
        return extractRequestInfo;
    }
}
