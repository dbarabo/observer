package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 47. Сведения об отказе источника от предложения совершить сделку
 */
public class Ul47Reject {

    @XStreamAlias("rejectDate")
    private final StringElement rejectDate; //57.1. Дата отказа

    @XStreamAlias("rejectCode")
    private final StringElement rejectCode; //57.2. Код причины отказа

    public Ul47Reject(Date rejectDate, Integer rejectCode) {

        this.rejectDate = new StringElement(XmlLoader.formatDate(rejectDate));

        this.rejectCode = new StringElement(rejectCode.toString());
    }
}
