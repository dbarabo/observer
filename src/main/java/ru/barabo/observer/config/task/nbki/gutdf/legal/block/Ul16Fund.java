package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 16. Дата передачи финансирования субъекту или возникновения обеспечения исполнения обязательства
 */
public class Ul16Fund {

    @XStreamAlias("date")
    private final StringElement date; //minOccurs="0" 24.1. Дата передачи финансирования субъекту или возникновения обеспечения исполнения обязательства

    @XStreamAlias("num")
    private final StringElement num; //minOccurs="0" 24.2. Порядковый номер транша

    @XStreamAlias("startSum")
    private final StringElement startSum; //emptyValueType| dec15p2Type 24.3. Сумма задолженности на дату передачи финансирования субъекту или возникновения обеспечения исполнения обязательства

    public Ul16Fund(Date date, Integer num, Number startSum) {

        this.date = date == null ? null : new StringElement(XmlLoader.formatDate(date));

        this.num = num == null ? null : new StringElement(num.toString());

        this.startSum = new StringElement(startSum == null ? "-" : XmlLoader.formatSum(startSum));
    }

    public StringElement getDate() {
        return date;
    }

    public StringElement getNum() {
        return num;
    }

    public StringElement getStartSum() {
        return startSum;
    }
}
