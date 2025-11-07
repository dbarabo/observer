package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 17. Сведения о задолженности
 */
public class Ul17Debt {

    @XStreamAlias("debtSum")
    private final StringElement debtSum; // dec15p2Type 25.4. Сумма задолженности

    @XStreamAlias("debtMainSum")
    private final StringElement debtMainSum; // dec15p2Type 25.5. Сумма задолженности по основному долгу

    @XStreamAlias("debtPercentSum")
    private final StringElement debtPercentSum; // dec15p2Type 5.6. Сумма задолженности по процентам<

    @XStreamAlias("debtOtherSum")
    private final StringElement debtOtherSum; // dec15p2Type 25.7. Сумма задолженности по иным требованиям

    @XStreamAlias("currency")
    private final StringElement currency = new StringElement("RUB");

    public Ul17Debt() {
        this.debtSum = null;
        this.debtMainSum = null;
        this.debtPercentSum = null;
        this.debtOtherSum = null;
    }

    public Ul17Debt(Number debtSum, Number debtMainSum, Number debtPercentSum, Number debtOtherSum) {

        this.debtSum = new StringElement(XmlLoader.formatSum(debtSum));
        this.debtMainSum = new StringElement(XmlLoader.formatSum(debtMainSum));
        this.debtPercentSum = new StringElement(XmlLoader.formatSum(debtPercentSum));
        this.debtOtherSum = new StringElement(XmlLoader.formatSum(debtOtherSum));
    }

    public StringElement getDebtSum() {
        return debtSum;
    }

    public StringElement getDebtMainSum() {
        return debtMainSum;
    }

    public StringElement getDebtPercentSum() {
        return debtPercentSum;
    }

    public StringElement getDebtOtherSum() {
        return debtOtherSum;
    }
}
