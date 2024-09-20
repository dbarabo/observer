package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

public class Fl19_1AmountInfo {

    @XStreamAlias("securityFact_0")
    private final StringElement securityFact0; //19(1).1. Признак обеспечивающего обязательства = 0

    @XStreamAlias("securityFact_1")
    private final StringElement securityFact1; //19(1).1. Признак обеспечивающего обязательства = 1

    @XStreamAlias("securitySum")
    private final StringElement securitySum; // minOccurs="0" dec15p2Type >19(1).2. Сумма обеспечиваемого обязательства

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

    public Fl19_1AmountInfo() {
        this.securityFact0 = new StringElement("");
        this.securityFact1 = null;

        this.securitySum = null;
        this.securityCurrency = null;
        this.securityTypeCode = null;
        this.calcDate = null;
        this.securityUid = null;
        this.liabilityLimit = null;
    }

    public Fl19_1AmountInfo(Number securitySum, Integer securityTypeCode, Date calcDate, String securityUid, Long liabilityLimit) {

        this.securityFact0 = null;
        this.securityFact1 = new StringElement("");

        this.securitySum = securitySum == null ? null : new StringElement(XmlLoader.formatSum(securitySum));

        this.securityCurrency = securityTypeCode == null ? null : new StringElement("RUB");
        this.securityTypeCode = securityTypeCode == null ? null : new StringElement(securityTypeCode.toString());
        this.calcDate = new StringElement( calcDate == null ? "-" : XmlLoader.formatDate(calcDate));
        this.securityUid = securityUid == null ? null : new StringElement(securityUid);
        this.liabilityLimit = liabilityLimit == null ? null : new StringElement(liabilityLimit.toString());
    }
}
