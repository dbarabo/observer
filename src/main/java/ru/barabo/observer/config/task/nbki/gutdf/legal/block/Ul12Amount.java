package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 12. Сумма и валюта обязательства
 */
public class Ul12Amount {

    @XStreamAlias("sum")
    private final StringElement sum; //19.1. Сумма обязательства emptyValueType dec15p2Type

    @XStreamAlias("currency")
    private final StringElement currency; //19.2. Валюта обязательства emptyValueType currencyCodeType

    @XStreamAlias("calcDate")
    private final StringElement calcDate; //19.6. Дата расчета

    public Ul12Amount(Number sum, Date calcDate) {

        this.sum = new StringElement(sum == null ? "-" : XmlLoader.formatSum(sum));

        this.currency = new StringElement(sum == null ? "-" : "RUB");

        this.calcDate = new StringElement(XmlLoader.formatDate(calcDate));
    }

    public StringElement getSum() {
        return sum;
    }

    public StringElement getCalcDate() {
        return calcDate;
    }
}
