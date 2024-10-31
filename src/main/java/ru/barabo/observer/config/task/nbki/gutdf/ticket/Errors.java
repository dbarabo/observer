package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Errors")
public class Errors {

    @XStreamImplicit(itemFieldName = "Error")
    private List<ErrorInfo> errorList;

    public List<ErrorInfo> getErrorList() {
        return errorList;
    }
}
