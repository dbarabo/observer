package ru.barabo.observer.config.task.p311.ticket.sfr;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ns2:Файл")
public class MainFileTicketSfr {
    @XStreamAsAttribute
    @XStreamAlias("ИдФайл")
    private String idFile;

    @XStreamAsAttribute
    @XStreamAlias("ТипИнф")
    private String typeTicket;

    @XStreamAsAttribute
    @XStreamAlias("ВерсПрог")
    private String versionProg;

    @XStreamAsAttribute
    @XStreamAlias("ТелОтпр")
    private String phoneSender;

    @XStreamAsAttribute
    @XStreamAlias("ДолжнОтпр")
    private String sender;

    @XStreamAsAttribute
    @XStreamAlias("ФамОтпр")
    private String nameSender;

    @XStreamAsAttribute
    @XStreamAlias("КолДок")
    private String countDoc;

    @XStreamAsAttribute
    @XStreamAlias("ВерсФорм")
    private String versForm;

    @XStreamAsAttribute
    @XStreamAlias("xmlns:ns2")
    private String xmlnsNs2;

    @XStreamAlias("ns2:Документ")
    private DocumentTicketSfr documentTicketSfr;

    public DocumentTicketSfr getDocumentTicketSfr() {
        return documentTicketSfr;
    }
}

