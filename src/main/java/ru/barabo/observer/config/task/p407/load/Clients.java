package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Клиенты")
public class Clients {

    @XStreamAlias("ЧислоКлиентов")
    private String count;

    @XStreamImplicit(itemFieldName = "КлиентКО")
    private List<ClientBank> clientBank;

    public List<ClientBank> getClientBank() {
        return clientBank;
    }
}
