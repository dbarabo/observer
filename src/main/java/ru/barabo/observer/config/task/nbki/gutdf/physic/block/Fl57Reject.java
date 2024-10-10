package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

public class Fl57Reject {

    @XStreamAlias("rejectDate")
    private final StringElement rejectDate; //57.1. Дата отказа

    @XStreamAlias("rejectCode")
    private final StringElement rejectCode; //57.2. Код причины отказа

    public Fl57Reject(Date rejectDate, Integer rejectCode) {

        this.rejectDate = new StringElement(XmlLoader.formatDate(rejectDate));

        this.rejectCode = new StringElement(rejectCode.toString());
    }

    public StringElement getRejectDate() {
        return rejectDate;
    }

    public StringElement getRejectCode() {
        return rejectCode;
    }
}
