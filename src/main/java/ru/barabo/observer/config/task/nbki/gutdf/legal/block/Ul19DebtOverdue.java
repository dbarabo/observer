package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 19. Сведения о просроченной задолженности
 */
public class Ul19DebtOverdue {

    @XStreamAlias("missFact_0")
    private final StringElement missFact0; // 27.10. Признак наличия просроченной задолженности = 0

    @XStreamAlias("missFact_1")
    private final StringElement missFact1; // 27.10. Признак наличия просроченной задолженности = 1

    @XStreamAlias("debtOverdueSum")
    private final StringElement debtOverdueSum; // dec15p2Type 27.3. Сумма просроченной задолженности

    @XStreamAlias("debtOverdueMainSum")
    private final StringElement debtOverdueMainSum; // minOccurs="0" dec15p2Type 27.4. Сумма просроченной задолженности по основному долгу

    @XStreamAlias("debtOverduePercentSum")
    private final StringElement debtOverduePercentSum; // minOccurs="0" dec15p2Type 27.5. Сумма просроченной задолженности по процентам

    @XStreamAlias("debtOverdueOtherSum")
    private final StringElement debtOverdueOtherSum; // minOccurs="0" dec15p2Type 27.6. Сумма просроченной задолженности по иным требованиям

    @XStreamAlias("debtOverdueStartDate")
    private final StringElement debtOverdueStartDate; //27.1. Дата возникновения просроченной задолженности

    @XStreamAlias("mainMissDate")
    private final StringElement mainMissDate; // minOccurs="0" 27.8. Дата последнего пропущенного платежа по основному долгу

    @XStreamAlias("percentMissDate")
    private final StringElement percentMissDate; // minOccurs="0" 27.8. 27.9. Дата последнего пропущенного платежа по процентам<

    @XStreamAlias("missDuration")
    private final StringElement missDuration; // minOccurs="0" 27.11. Продолжительность просрочки в днях

    @XStreamAlias("repaidMissDuration")
    private final StringElement repaidMissDuration; // minOccurs="0" 27.12. Продолжительность последней погашенной просрочки в днях

    public Ul19DebtOverdue() {
        this.missFact0 = new StringElement("");
        this.missFact1 = null;

        this.debtOverdueSum = null;
        this.debtOverdueMainSum = null;
        this.debtOverduePercentSum = null;
        this.debtOverdueOtherSum = null;
        this.debtOverdueStartDate = null;
        this.mainMissDate = null;
        this.percentMissDate = null;
        this.missDuration = null;
        this.repaidMissDuration = null;
    }

    public Ul19DebtOverdue(Number debtOverdueSum, Number debtOverdueMainSum, Number debtOverduePercentSum,
                           Number debtOverdueOtherSum, Date debtOverdueStartDate, Date mainMissDate,
                           Date percentMissDate, Integer missDuration, Integer repaidMissDuration) {
        this.missFact0 = null;
        this.missFact1 = new StringElement("");

        this.debtOverdueSum = new StringElement(XmlLoader.formatSum(debtOverdueSum));

        this.debtOverdueMainSum = debtOverdueMainSum == null ? null : new StringElement(XmlLoader.formatSum(debtOverdueMainSum));

        this.debtOverduePercentSum = debtOverduePercentSum == null ? null : new StringElement(XmlLoader.formatSum(debtOverduePercentSum));

        this.debtOverdueOtherSum = debtOverdueOtherSum == null ? null : new StringElement(XmlLoader.formatSum(debtOverdueOtherSum));

        this.debtOverdueStartDate = debtOverdueStartDate == null ? null : new StringElement(XmlLoader.formatDate(debtOverdueStartDate));

        this.mainMissDate = mainMissDate == null ? null : new StringElement(XmlLoader.formatDate(mainMissDate));

        this.percentMissDate = percentMissDate == null ? null : new StringElement(XmlLoader.formatDate(percentMissDate));

        this.missDuration = missDuration == null ? null : new StringElement(missDuration.toString());

        this.repaidMissDuration = missDuration == null ? null : new StringElement(repaidMissDuration.toString());
    }
}
