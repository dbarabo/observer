package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("NotificationOfAcceptance")
public class MainNotificationOfAcceptance {

    @XStreamAlias("schemaVersion")
    @XStreamOmitField
    private String schemaVersion;

    @XStreamAlias("DocInfo")
    private DocInfo docInfo;

    @XStreamAlias("Errors")
    private Errors errors;

    public MainNotificationOfAcceptance() {}

    public DocInfo getDocInfo() {
        return docInfo;
    }

    public Errors getErrors() {
        return errors;
    }
}
