package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("DocInfo")
public class DocInfo {

    @XStreamAlias("inn")
    public StringElement inn;

    @XStreamAlias("ogrn")
    public StringElement ogrn;

    @XStreamAlias("sourceId")
    public StringElement sourceId;

    @XStreamAlias("dataReceivedStatus")
    public StringElement dataReceivedStatus;

    @XStreamAlias("outgoingDocDate")
    public StringElement outgoingDocDate;

    @XStreamAlias("outgoingDocNumber")
    public StringElement outgoingDocNumber;

    @XStreamAlias("incomingDocDate")
    public StringElement incomingDocDate;

    @XStreamAlias("incomingDocNumber")
    public StringElement incomingDocNumber;

    @XStreamAlias("dateTime")
    public StringElement dateTime;

    @XStreamAlias("fileName")
    public StringElement fileName;

    @XStreamAlias("fileHash")
    public StringElement fileHash;

    @XStreamAlias("subjectsReceived")
    public StringElement subjectsReceived;

    @XStreamAlias("eventsReceived")
    public StringElement eventsReceived;

    @XStreamAlias("eventsAccepted")
    public StringElement eventsAccepted;

    @XStreamAlias("eventsRejected")
    public StringElement eventsRejected;

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
