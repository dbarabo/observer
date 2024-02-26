package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("КлиентКО")
public class ClientBank {

    @XStreamAlias("НомерКлиент")
    private Integer number;

    @XStreamAlias("ПризнИдентКлиента")
    private Integer isIdentity;

    @XStreamAlias("ТипКлиент")
    private Integer typeClient;

    @XStreamAlias("ЮрЛицо")
    private ClientBankJuric clientBankJuric;

    @XStreamAlias("ФизЛицо")
    private ClientBankPhysic clientBankPhysic;

    @XStreamAlias("ВидЗапрИнфо")
    private RequestInfo requestInfo;

    public ClientBankJuric getClientBankJuric() {
        return clientBankJuric;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ClientBankPhysic getClientBankPhysic() {
        return clientBankPhysic;
    }
}
