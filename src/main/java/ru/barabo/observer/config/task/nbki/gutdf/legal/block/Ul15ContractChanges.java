package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 15. Сведения об изменении договора
 */
public class Ul15ContractChanges {

    @XStreamAlias("exist_0")
    private final StringElement exist0; // 23.1. Признак изменения договора = 0

    @XStreamAlias("exist_1")
    private final StringElement exist1; // 23.1. Признак изменения договора = 1

    @XStreamAlias("changeDate")
    private final StringElement changeDate; // 23.2. Дата изменения договора emptyValueType|

    @XStreamAlias("code")
    private final StringElement code; // 23.3. Код вида изменения договора<

    @XStreamAlias("specialCode")
    private final StringElement specialCode; // 23.4. Код специального изменения договора

    @XStreamAlias("otherDesc")
    private final StringElement otherDesc; // minOccurs="0" 23.5. Описание иного изменения договора

    @XStreamAlias("startDate")
    private final StringElement startDate; // 23.6. Дата вступления изменения договора в силу emptyValueType|

    @XStreamAlias("endDate")
    private final StringElement endDate; // 23.7. Дата планового прекращения действия изменения договора emptyValueType|

    @XStreamAlias("actualEndDate")
    private final StringElement actualEndDate; // minOccurs="0" 23.8. Дата фактического прекращения действия изменения договора

    @XStreamAlias("endCode")
    private final StringElement endCode; // minOccurs="0" 23.9. Код причины прекращения действия изменения договора

    @XStreamAlias("currencyRate")
    private final StringElement currencyRate; // minOccurs="0" dec4p4Type 23.10. Курс конверсии валюты долга

    public Ul15ContractChanges() {
        this.exist0 = new StringElement("");
        this.exist1 = null;
        this.changeDate = null;
        this.code = null;
        this.specialCode = null;
        this.otherDesc = null;
        this.startDate = null;
        this.endDate = null;
        this.actualEndDate = null;
        this.endCode = null;
        this.currencyRate = null;
    }

    public Ul15ContractChanges(Date changeDate, Integer code, Integer specialCode, String otherDesc,
                               Date startDate, Date endDate, Date actualEndDate, Integer endCode) {
        this.exist0 = null;
        this.exist1 = new StringElement("");

        this.changeDate = new StringElement(changeDate == null ? "-" : XmlLoader.formatDate(changeDate) );

        this.code = new StringElement(code.toString());

        this.specialCode = new StringElement(specialCode.toString());

        this.otherDesc = otherDesc == null ? null : new StringElement(otherDesc);

        this.startDate = new StringElement(startDate == null ? "-" : XmlLoader.formatDate(startDate) );

        this.endDate = new StringElement(endDate == null ? "-" : XmlLoader.formatDate(endDate) );

        this.actualEndDate = actualEndDate == null ? null : new StringElement(XmlLoader.formatDate(actualEndDate));

        this.endCode = endCode == null ? null : new StringElement(endCode.toString());
        this.currencyRate = null;
    }
}
