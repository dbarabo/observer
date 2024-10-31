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
    private String schemaVersion;

    @XStreamAlias("inn")
    private StringElement inn;

    @XStreamAlias("ogrn")
    private StringElement ogrn;

    @XStreamAlias("sourceId")
    private StringElement sourceId;

    @XStreamAlias("outgoingDocDate")
    private StringElement outgoingDocDate;

    @XStreamAlias("outgoingDocNumber")
    private StringElement outgoingDocNumber;

    @XStreamAlias("acceptanceStatusFail")
    private StringElement acceptanceStatusFail;

    @XStreamImplicit(itemFieldName = "Errors")
    private List<ErrorReceipt> errorReceiptList;

    @XStreamAlias("acceptanceStatusOK")
    private StringElement acceptanceStatusOK;

    @XStreamAlias("incomingDocDate")
    private StringElement incomingDocDate;

    @XStreamAlias("incomingDocNumber")
    private StringElement incomingDocNumber;

    @XStreamAlias("fileName")
    private StringElement fileName;

    @XStreamAlias("fileHash")
    private StringElement fileHash;

    @XStreamAlias("dateTime")
    private StringElement dateTime;

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
