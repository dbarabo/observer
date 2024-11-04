package ru.barabo.observer.config.task.nbki.gutdf.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class ErrorInfo {

    @XStreamAlias("errorNum")
    public String errorNum; // Порядковый номер ошибки в записи

    @XStreamAlias("errorCode")
    public StringElement errorCode;

    @XStreamAlias("errorMessage")
    public StringElement errorMessage;

    @XStreamAlias("uid")
    public StringElement uid;

    @XStreamAlias("orderNum")
    public StringElement orderNum;

    @XStreamAlias("orderNum_3_2")
    public StringElement orderNum32;

    @XStreamAlias("subjectCode")
    public StringElement subjectCode;

    @XStreamAlias("eventName")
    public StringElement eventName;

    @XStreamAlias("blockName")
    public StringElement blockName;

    @XStreamAlias("fieldName")
    public StringElement fieldName;

    @XStreamAlias("fieldValue")
    public StringElement fieldValue;

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
