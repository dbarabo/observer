package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("DocInfo")
public class DocInfo {

    @XStreamAlias("inn")
    private StringElement inn;

    @XStreamAlias("ogrn")
    private StringElement ogrn;

    @XStreamAlias("sourceId")
    private StringElement sourceId;

    @XStreamAlias("dataReceivedStatus")
    private StringElement dataReceivedStatus;

    @XStreamAlias("outgoingDocDate")
    private StringElement outgoingDocDate;

    @XStreamAlias("outgoingDocNumber")
    private StringElement outgoingDocNumber;

    @XStreamAlias("incomingDocDate")
    private StringElement incomingDocDate;

    @XStreamAlias("incomingDocNumber")
    private StringElement incomingDocNumber;

    @XStreamAlias("dateTime")
    private StringElement dateTime;

    @XStreamAlias("fileName")
    private StringElement fileName;

    @XStreamAlias("fileHash")
    private StringElement fileHash;

    @XStreamAlias("subjectsReceived")
    private StringElement subjectsReceived;

    @XStreamAlias("eventsReceived")
    private StringElement eventsReceived;

    @XStreamAlias("eventsAccepted")
    private StringElement eventsAccepted;

    @XStreamAlias("eventsRejected")
    private StringElement eventsRejected;

    public DocInfo() {}

    public StringElement getDataReceivedStatus() {
        return dataReceivedStatus;
    }

    public StringElement getOutgoingDocDate() {
        return outgoingDocDate;
    }

    public StringElement getOutgoingDocNumber() {
        return outgoingDocNumber;
    }

    public StringElement getIncomingDocDate() {
        return incomingDocDate;
    }

    public StringElement getIncomingDocNumber() {
        return incomingDocNumber;
    }

    public StringElement getDateTime() {
        return dateTime;
    }

    public StringElement getFileName() {
        return fileName;
    }

    public StringElement getSubjectsReceived() {
        return subjectsReceived;
    }

    public StringElement getEventsReceived() {
        return eventsReceived;
    }

    public StringElement getEventsAccepted() {
        return eventsAccepted;
    }

    public StringElement getEventsRejected() {
        return eventsRejected;
    }
}
