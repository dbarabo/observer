package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 26. Сведения о срочной задолженности
 */
public class Fl26DebtDue {

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

    public Fl26DebtDue(Number debtDueSum, Number debtDueMainSum, Number debtDuePercentSum, Number debtDueOtherSum,
                       Date debtDueStartDate) {

        this.debtDueSum = new StringElement(XmlLoader.formatSum(debtDueSum == null ? 0 : debtDueSum));

        this.debtDueMainSum = debtDueMainSum == null ? null : new StringElement(XmlLoader.formatSum(debtDueMainSum));

        this.debtDuePercentSum = debtDuePercentSum == null ? null : new StringElement(XmlLoader.formatSum(debtDuePercentSum));

        this.debtDueOtherSum = debtDueOtherSum == null ? null : new StringElement(XmlLoader.formatSum(debtDueOtherSum));

        this.debtDueStartDate = debtDueStartDate == null ? null : new StringElement(XmlLoader.formatDate(debtDueStartDate));
    }
}
