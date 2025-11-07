package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 25. Сведения о задолженности
 */
public class Fl25Debt {

    @XStreamAlias("debtSum")
    private final StringElement debtSum; // dec15p2Type 25.4. Сумма задолженности

    @XStreamAlias("debtMainSum")
    private final StringElement debtMainSum; // dec15p2Type 25.5. Сумма задолженности по основному долгу

    @XStreamAlias("debtPercentSum")
    private final StringElement debtPercentSum; // dec15p2Type 5.6. Сумма задолженности по процентам<

    @XStreamAlias("debtOtherSum")
    private final StringElement debtOtherSum; // dec15p2Type 25.7. Сумма задолженности по иным требованиям

    @XStreamAlias("graceUnconfExist_0")
    private final StringElement graceUnconfExist0; // minOccurs="0" 25.9. Признак неподтвержденного льготного периода = 0

    @XStreamAlias("graceUnconfExist_1")
    private final StringElement graceUnconfExist1; // minOccurs="0"25.9. Признак неподтвержденного льготного периода = 1

    @XStreamAlias("currency")
    private final StringElement currency = new StringElement("RUB");

    public Fl25Debt() {
        this.debtSum = null;
        this.debtMainSum = null;
        this.debtPercentSum = null;
        this.debtOtherSum = null;
        this.graceUnconfExist0 = null;
        this.graceUnconfExist1 = null;
    }

    public Fl25Debt(Number debtSum, Number debtMainSum, Number debtPercentSum, Number debtOtherSum) {

        this.debtSum = new StringElement(XmlLoader.formatSum(debtSum));
        this.debtMainSum = new StringElement(XmlLoader.formatSum(debtMainSum));
        this.debtPercentSum = new StringElement(XmlLoader.formatSum(debtPercentSum));
        this.debtOtherSum = new StringElement(XmlLoader.formatSum(debtOtherSum));

        this.graceUnconfExist0 = null;
        this.graceUnconfExist1 = null;
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
