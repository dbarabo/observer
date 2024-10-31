package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class ErrorInfo {

    @XStreamAlias("errorNum")
    private String errorNum; // Порядковый номер ошибки в записи

    @XStreamAlias("errorCode")
    private StringElement errorCode;

    @XStreamAlias("errorMessage")
    private StringElement errorMessage;

    @XStreamAlias("uid")
    private StringElement uid;

    @XStreamAlias("orderNum")
    private StringElement orderNum;

    @XStreamAlias("orderNum_3_2")
    private StringElement orderNum32;

    @XStreamAlias("subjectCode")
    private StringElement subjectCode;

    @XStreamAlias("eventName")
    private StringElement eventName;

    @XStreamAlias("blockName")
    private StringElement blockName;

    @XStreamAlias("fieldName")
    private StringElement fieldName;

    @XStreamAlias("fieldValue")
    private StringElement fieldValue;

    public String getErrorNum() {
        return errorNum;
    }

    public StringElement getErrorCode() {
        return errorCode;
    }

    public StringElement getErrorMessage() {
        return errorMessage;
    }

    public StringElement getUid() {
        return uid;
    }

    public StringElement getOrderNum() {
        return orderNum;
    }

    public StringElement getOrderNum32() {
        return orderNum32;
    }

    public StringElement getSubjectCode() {
        return subjectCode;
    }

    public StringElement getEventName() {
        return eventName;
    }

    public StringElement getBlockName() {
        return blockName;
    }

    public StringElement getFieldName() {
        return fieldName;
    }

    public StringElement getFieldValue() {
        return fieldValue;
    }
}
