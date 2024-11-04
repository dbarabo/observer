package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.List;

@XStreamAlias("NotificationOfReceipt")
public class MainNotificationOfReceipt {

    @XStreamAlias("schemaVersion")
    @XStreamOmitField
    public String schemaVersion;

    @XStreamAlias("inn")
    public StringElement inn;

    @XStreamAlias("ogrn")
    public StringElement ogrn;

    @XStreamAlias("sourceId")
    public StringElement sourceId;

    @XStreamAlias("outgoingDocDate")
    public StringElement outgoingDocDate;

    @XStreamAlias("outgoingDocNumber")
    public StringElement outgoingDocNumber;

    @XStreamAlias("acceptanceStatusFail")
    public StringElement acceptanceStatusFail;

    @XStreamImplicit(itemFieldName = "Errors")
    public List<ErrorReceipt> errorReceiptList;

    @XStreamAlias("acceptanceStatusOK")
    public StringElement acceptanceStatusOK;

    @XStreamAlias("incomingDocDate")
    public StringElement incomingDocDate;

    @XStreamAlias("incomingDocNumber")
    public StringElement incomingDocNumber;

    @XStreamAlias("fileName")
    public StringElement fileName;

    @XStreamAlias("fileHash")
    public StringElement fileHash;

    @XStreamAlias("dateTime")
    public StringElement dateTime;

    public StringElement getFileName() {
        return fileName;
    }

    public StringElement getOutgoingDocDate() {
        return outgoingDocDate;
    }

    public StringElement getOutgoingDocNumber() {
        return outgoingDocNumber;
    }

    public StringElement getAcceptanceStatusFail() {
        return acceptanceStatusFail;
    }

    public List<ErrorReceipt> getErrorReceiptList() {
        return errorReceiptList;
    }

    public StringElement getAcceptanceStatusOK() {
        return acceptanceStatusOK;
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
}
