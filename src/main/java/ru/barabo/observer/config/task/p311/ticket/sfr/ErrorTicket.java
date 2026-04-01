package ru.barabo.observer.config.task.p311.ticket.sfr;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ns2:Ошибки")
public class ErrorTicket {

    @XStreamAsAttribute
    @XStreamAlias("КодОшибки")
    private String code;

    @XStreamAsAttribute
    @XStreamAlias("НаимОшибки")
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
