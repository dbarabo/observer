package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Стоимость предмета залога
 */
public class SumGroupUl23_26Group {

    @XStreamAlias("sum")
    private final StringElement sum; // dec15p2Type 32.5. Стоимость предмета залога

    @XStreamAlias("currency")
    private final StringElement currency; // dec15p2Type 32.6. Валюта стоимости предмета залога

    @XStreamAlias("assessDate")
    private final StringElement assessDate; //emptyValueType 32.7. Дата проведения оценки предмета залога

    @XStreamAlias("priceCode")
    private final StringElement priceCode; // dec15p2Type 32.5. Стоимость предмета залога

    public SumGroupUl23_26Group(Number sum, Date assessDate, Integer priceCode) {

        this.sum = new StringElement(XmlLoader.formatSum(sum));

        this.currency = new StringElement("RUB");

        this.assessDate = new StringElement(assessDate == null ? "-" : XmlLoader.formatDate(assessDate));

        this.priceCode = new StringElement(priceCode.toString());
    }
}
