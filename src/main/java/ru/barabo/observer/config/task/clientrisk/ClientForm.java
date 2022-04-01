package ru.barabo.observer.config.task.clientrisk;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Client")
public class ClientForm {

    @XStreamAsAttribute
    @XStreamAlias("ClientType")
    final String clientType; // 0-JUR 1-IP

    @XStreamAsAttribute
    @XStreamAlias("INN")
    final String inn;

    @XStreamAsAttribute
    @XStreamAlias("ClientName")
    final String clientName;

    public ClientForm(Boolean isIp, String inn, String clientName) {

        this.clientType = isIp ? "1" : "0";

        this.inn = inn;

        this.clientName = clientName;
    }
}
