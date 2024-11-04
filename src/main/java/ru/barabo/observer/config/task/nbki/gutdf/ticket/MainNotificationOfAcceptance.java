package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("NotificationOfAcceptance")
public class MainNotificationOfAcceptance {

    @XStreamAlias("schemaVersion")
    @XStreamOmitField
    public String schemaVersion;

    @XStreamAlias("DocInfo")
    public DocInfo docInfo;

    @XStreamAlias("Errors")
    public Errors errors;

    public MainNotificationOfAcceptance() {}

    public DocInfo getDocInfo() {
        return docInfo;
    }

    public Errors getErrors() {
        return errors;
    }
}
