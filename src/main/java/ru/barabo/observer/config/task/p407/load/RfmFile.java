package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

@XStreamAlias("ДокументЗапросИнформации")
public class RfmFile {

    @XStreamAlias("СлужИнфо")
    @XStreamOmitField
    private Object serviceInfo;

    @XStreamAlias("СведенияКО")
    @XStreamOmitField
    private Object bankInfo;

    @XStreamAlias("Клиенты")
    private Clients clients;

    @XStreamAlias("Срок")
    @XStreamOmitField
    private Object dueDate;

    public List<ClientBank> getClientList()  {
        return clients.getClientBank();
    }

}
