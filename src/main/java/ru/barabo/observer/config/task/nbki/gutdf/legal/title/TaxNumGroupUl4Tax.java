package ru.barabo.observer.config.task.nbki.gutdf.legal.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Номер налогоплательщика
 */
public class TaxNumGroupUl4Tax {

    @XStreamAlias("taxCode")
    private final StringElement taxCode; //4.1. Код номера налогоплательщика =1 если из РФ

    @XStreamAlias("taxNum")
    private final StringElement taxNum; //4.2. Номер налогоплательщика

    public TaxNumGroupUl4Tax(String taxNum) {

        this.taxCode = new StringElement("1");
        this.taxNum = new StringElement(taxNum);
    }

    public TaxNumGroupUl4Tax(StringElement taxCode, StringElement taxNum) {
        this.taxCode = taxCode;
        this.taxNum = taxNum;
    }

    public StringElement getTaxNum() {
        return taxNum;
    }

    public StringElement getTaxCode() {
        return taxCode;
    }
}
