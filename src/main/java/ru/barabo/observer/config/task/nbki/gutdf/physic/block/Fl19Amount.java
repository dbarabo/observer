package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

public class Fl19Amount {

    @XStreamAlias("sum")
    private final StringElement sum; //19.1. Сумма обязательства emptyValueType dec15p2Type

    @XStreamAlias("currency")
    private final StringElement currency; //19.2. Валюта обязательства emptyValueType currencyCodeType

    @XStreamAlias("calcDate")
    private final StringElement calcDate; //19.6. Дата расчета

    public Fl19Amount(Number sum, Date calcDate) {

        this.sum = new StringElement(sum == null ? "-" : XmlLoader.formatSum(sum));

        this.currency = new StringElement(sum == null ? "-" : "RUB");

        this.calcDate = new StringElement(XmlLoader.formatDate(calcDate));
    }
}
