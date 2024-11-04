package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class ErrorReceipt {

    @XStreamAlias("errorCode")
    public StringElement errorCode;

    @XStreamAlias("errorMessage")
    public StringElement errorMessage;

    public StringElement getErrorCode() {
        return errorCode;
    }

    public StringElement getErrorMessage() {
        return errorMessage;
    }
}
