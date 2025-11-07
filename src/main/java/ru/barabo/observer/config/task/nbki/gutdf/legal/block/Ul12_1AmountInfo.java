package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 12(1). Сведения об обеспечиваемом обязательстве
 */
public class Ul12_1AmountInfo {

    @XStreamAlias("securityFact_0")
    private final StringElement securityFact0; //19(1).1. Признак обеспечивающего обязательства = 0

    @XStreamAlias("securityFact_1")
    private final StringElement securityFact1; //19(1).1. Признак обеспечивающего обязательства = 1

    @XStreamAlias("securitySum")
    private final StringElement securitySum; //dec15p2Type minOccurs="0" 12(1).2. Сумма обеспечиваемого обязательства

    @XStreamAlias("securityCurrency")
    private final StringElement securityCurrency; // minOccurs="0" 19(1).3. Валюта обеспечиваемого обязательства

    @XStreamAlias("securityTypeCode")
    private final StringElement securityTypeCode; // minOccurs="0"19(1).4. Код типа обеспечиваемого обязательства

    @XStreamAlias("calcDate")
    private final StringElement calcDate; // 19(1).5. Дата расчета emptyValueType xs:date

    @XStreamAlias("securityUid")
    private final StringElement securityUid; // minOccurs="0" 19(1).6. УИд сделки, в результате которой возникло обеспечиваемое обязательство

    @XStreamAlias("liabilityLimit")
    private final StringElement liabilityLimit; // minOccurs="0" 19(1).7. Лимит ответственности по обеспечиваемому обязательству

    @XStreamAlias("limitCurrency")
    private final StringElement limitCurrency;

    public Ul12_1AmountInfo() {
        this.securityFact0 = new StringElement("");
        this.securityFact1 = null;

        this.securitySum = null;
        this.securityCurrency = null;
        this.securityTypeCode = null;
        this.calcDate = null;
        this.securityUid = null;
        this.liabilityLimit = null;
        this.limitCurrency = null;
    }

    public Ul12_1AmountInfo(Number securitySum, Integer securityTypeCode, Date calcDate, String securityUid, Long liabilityLimit) {

        this.securityFact0 = null;
        this.securityFact1 = new StringElement("");

        this.securitySum = securitySum == null ? null : new StringElement(XmlLoader.formatSum(securitySum));

        this.securityCurrency = securitySum == null ? null : new StringElement("RUB");
        this.securityTypeCode = securityTypeCode == null ? null : new StringElement(securityTypeCode.toString());
        this.calcDate = new StringElement( calcDate == null ? "-" : XmlLoader.formatDate(calcDate));
        this.securityUid = securityUid == null ? null : new StringElement(securityUid);
        this.liabilityLimit = liabilityLimit == null ? null : new StringElement(liabilityLimit.toString());
        this.limitCurrency = liabilityLimit == null ? null : new StringElement("RUB");
    }
    public StringElement getSecuritySum() {
        return securitySum;
    }

    public StringElement getSecurityTypeCode() {
        return securityTypeCode;
    }

    public StringElement getCalcDate() {
        return calcDate;
    }

    public StringElement getSecurityUid() {
        return securityUid;
    }

    public StringElement getLiabilityLimit() {
        return liabilityLimit;
    }
}
