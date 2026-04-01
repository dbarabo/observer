package ru.barabo.observer.config.task.p311.ticket.sfr;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("ns2:Документ")
public class DocumentTicketSfr {

    @XStreamAsAttribute
    @XStreamAlias("ИдДок")
    private String idDoc;

    @XStreamAsAttribute
    @XStreamAlias("ДатаОбр") //"01.04.2026"
    private String dateProcessed;

    @XStreamAsAttribute
    @XStreamAlias("ИдФайлИсх")
    private String idFileSender;

    @XStreamAsAttribute
    @XStreamAlias("НомСооб")
    private String numberMessage;

    @XStreamAsAttribute
    @XStreamAlias("ТипСооб")
    private String typeMessage;

    @XStreamAsAttribute
    @XStreamAlias("ДатаСооб")
    private String dateMessage;

    @XStreamAsAttribute
    @XStreamAlias("КодОбр")
    private String codeProcessed;

    @XStreamAsAttribute
    @XStreamAlias("РезОбр")
    private String resultProcessed;

    @XStreamImplicit(itemFieldName = "ns2:Ошибки")
    private List<ErrorTicket> errors;

    public List<ErrorTicket> getErrors() {
        return errors;
    }

    public String getResultProcessed() {
        return resultProcessed;
    }

    public String getCodeProcessed() {
        return codeProcessed;
    }

    public String getDateMessage() {
        return dateMessage;
    }

    public String getNumberMessage() {
        return numberMessage;
    }

    public String getDateProcessed() {
        return dateProcessed;
    }
}
