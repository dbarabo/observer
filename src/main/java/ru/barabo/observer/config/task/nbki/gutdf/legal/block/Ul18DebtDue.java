package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 18. Сведения о срочной задолженности
 */
public class Ul18DebtDue {

    @XStreamAlias("debtDueSum")
    private final StringElement debtDueSum; // dec15p2Type 26.3. Сумма срочной задолженности

    @XStreamAlias("debtDueMainSum")
    private final StringElement debtDueMainSum; //  minOccurs="0" dec15p2Type 26.4. Сумма срочной задолженности по основному долгу<

    @XStreamAlias("debtDuePercentSum")
    private final StringElement debtDuePercentSum; //  minOccurs="0" dec15p2Type 26.5. Сумма срочной задолженности по процентам

    @XStreamAlias("debtDueOtherSum")
    private final StringElement debtDueOtherSum; //  minOccurs="0" dec15p2Type 6.6. Сумма срочной задолженности по иным требованиям

    @XStreamAlias("debtDueStartDate")
    private final StringElement debtDueStartDate; //  minOccurs="0" 26.1. Дата возникновения срочной задолженности

    public Ul18DebtDue() {
        this.debtDueSum = new StringElement(XmlLoader.formatSum(0));

        this.debtDueMainSum = null;

        this.debtDuePercentSum = null;

        this.debtDueOtherSum = null;

        this.debtDueStartDate = null;
    }

    public Ul18DebtDue(Number debtDueSum, Number debtDueMainSum, Number debtDuePercentSum, Number debtDueOtherSum,
                       Date debtDueStartDate) {

        this.debtDueSum = new StringElement(XmlLoader.formatSum(debtDueSum == null ? 0 : debtDueSum));

        this.debtDueMainSum = debtDueMainSum == null ? null : new StringElement(XmlLoader.formatSum(debtDueMainSum));

        this.debtDuePercentSum = debtDuePercentSum == null ? null : new StringElement(XmlLoader.formatSum(debtDuePercentSum));

        this.debtDueOtherSum = debtDueOtherSum == null ? null : new StringElement(XmlLoader.formatSum(debtDueOtherSum));

        this.debtDueStartDate = debtDueStartDate == null ? null : new StringElement(XmlLoader.formatDate(debtDueStartDate));
    }

    public StringElement getDebtDueSum() {
        return debtDueSum;
    }

    public StringElement getDebtDueMainSum() {
        return debtDueMainSum;
    }

    public StringElement getDebtDuePercentSum() {
        return debtDuePercentSum;
    }

    public StringElement getDebtDueOtherSum() {
        return debtDueOtherSum;
    }

    public StringElement getDebtDueStartDate() {
        return debtDueStartDate;
    }
}
