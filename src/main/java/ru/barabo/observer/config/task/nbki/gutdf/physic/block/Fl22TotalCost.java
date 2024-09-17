package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 22. Полная стоимость потребительского кредита (займа)
 */
public class Fl22TotalCost {

    //minOccurs="0" блоки сначала и до maxPercentCash - всей группой остальные по отдельности

    @XStreamAlias("minPercentCost")
    private final StringElement minPercentCost; // emptyValueType dec4p3Type 22.1. Минимальная полная стоимость кредита (займа) в процентах годовых

    @XStreamAlias("minCost")
    private final StringElement minCost; //emptyValueType dec15p2Type 22.2. Минимальная полная стоимость кредита (займа) в денежном выражении

    @XStreamAlias("calcDate")
    private final StringElement calcDate; //22.3. Дата расчета полной стоимости кредита (займа)

    @XStreamAlias("maxPercentCost")
    private final StringElement maxPercentCost; //emptyValueType dec4p3Type 22.1. 22.4. Максимальная полная стоимость кредита (займа) в процентах годовых

    @XStreamAlias("maxCost")
    private final StringElement maxCost; //emptyValueType dec15p2Type 2.5. Максимальная полная стоимость кредита (займа) в денежном выражени

    @XStreamAlias("maxPercentCash")
    private final StringElement maxPercentCash; //minOccurs="0" dec4p3Type 22.6. Максимальная полная стоимость кредита (займа) в процентах годовых при его использовании путем снятия наличных денежных средст

    @XStreamAlias("maxPercentCashless")
    private final StringElement maxPercentCashless; //minOccurs="0" dec4p3Type 22.7. Максимальная полная стоимость кредита (займа) в процентах годовых при его использовании в безналичном порядке

    public Fl22TotalCost() {
        this.minPercentCost = null;
        this.minCost = null;
        this.calcDate = null;
        this.maxPercentCost = null;
        this.maxCost = null;
        this.maxPercentCash = null;
        this.maxPercentCashless = null;
    }

    public Fl22TotalCost(Number minPercentCost, Number minCost, Date calcDate, Number maxPercentCost, Number maxCost) {
        this.minPercentCost = new StringElement(minPercentCost == null ? "-" : XmlLoader.formatDecimal3(minPercentCost));
        this.minCost = new StringElement(minCost == null ? "-" : XmlLoader.formatSum(minCost));
        this.calcDate = new StringElement(XmlLoader.formatDate(calcDate));
        this.maxPercentCost = new StringElement(maxPercentCost == null ? "-" : XmlLoader.formatDecimal3(maxPercentCost));;
        this.maxCost = new StringElement(maxCost == null ? "-" : XmlLoader.formatSum(maxCost));
        this.maxPercentCash = new StringElement(this.maxPercentCost.value);
        this.maxPercentCashless = new StringElement(this.maxPercentCost.value);
    }
}
