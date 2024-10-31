package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class ErrorReceipt {

    @XStreamAlias("errorCode")
    private StringElement errorCode;

    @XStreamAlias("errorMessage")
    private StringElement errorMessage;

    public StringElement getErrorCode() {
        return errorCode;
    }

    public StringElement getErrorMessage() {
        return errorMessage;
    }
}
