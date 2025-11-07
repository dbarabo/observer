package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 33. Сведения о поручительствах
 */
public class Fl33Warranty {

    @XStreamAlias("warrantyFact_0")
    private final StringElement warrantyFact0; // 33.1. Признак наличия поручительства = 0

    @XStreamAlias("warrantyFact_1")
    private final StringElement warrantyFact1; // 33.1. Признак наличия поручительства = 1

    @XStreamAlias("uid")
    private final StringElement uid; // 33.2. УИд договора поручительства

    @XStreamAlias("sum")
    private final StringElement sum; // emptyValueType dec15p2Type 33.3. Размер поручительства

    @XStreamAlias("currency")
    private final StringElement currency; // emptyValueType 33.4. Валюта поручительства

    @XStreamAlias("openDate")
    private final StringElement openDate; // emptyValueType 33.5. Дата заключения договора поручительства

    @XStreamAlias("endDate")
    private final StringElement endDate; // emptyValueType 33.6. Дата прекращения поручительства согласно договору

    @XStreamAlias("factEndDate")
    private final StringElement factEndDate; // minOccurs="0" 33.7. Дата фактического прекращения поручительства

    @XStreamAlias("endCode")
    private final StringElement endCode; // minOccurs="0" 33.8. Код причины прекращения поручительства


    public Fl33Warranty() {
        this.warrantyFact0 = new StringElement("");
        this.warrantyFact1 = null;

        this.uid = null;
        this.sum = null;
        this.currency = null;
        this.openDate = null;
        this.endDate = null;
        this.factEndDate = null;
        this.endCode = null;
    }

    public Fl33Warranty(String uid, Number sum, Date openDate, Date endDate, Date factEndDate, Integer endCode) {

        this.warrantyFact0 = null;
        this.warrantyFact1 = new StringElement("");

        this.uid = new StringElement(uid);

        this.sum = new StringElement(sum == null ? "-" : XmlLoader.formatSum(sum));

        this.currency = new StringElement("RUB");

        this.openDate = new StringElement(openDate == null ? "-" : XmlLoader.formatDate(openDate));

        this.endDate = new StringElement(endDate == null ? "-" : XmlLoader.formatDate(endDate));

        this.factEndDate = factEndDate == null ? null : new StringElement(XmlLoader.formatDate(factEndDate));

        this.endCode = endCode == null ? null : new StringElement(endCode.toString());
    }

    public StringElement getUid() {
        return uid;
    }

    public StringElement getSum() {
        return sum;
    }

    public StringElement getOpenDate() {
        return openDate;
    }

    public StringElement getEndDate() {
        return endDate;
    }

    public StringElement getFactEndDate() {
        return factEndDate;
    }

    public StringElement getEndCode() {
        return endCode;
    }
}
