package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 29. Величина среднемесячного платежа по договору займа (кредита) и дата ее расчета
 */
public class Fl29MonthlyPayment {

    @XStreamAlias("sum")
    private final StringElement sum; // emptyValueType|long 29.1. Величина среднемесячного платежа

    @XStreamAlias("calcDate")
    private final StringElement calcDate; // emptyValueType| 29.2. Дата расчета величины среднемесячного платежа

    @XStreamAlias("sumTotal")
    private final StringElement sumTotal; //  minOccurs="0" dec15p2Type 29.3. Сумма всех оставшихся платежей по обязательству

    public Fl29MonthlyPayment(Long sum, Date calcDate, Number sumTotal) {

        this.sum = new StringElement(sum == null ? "-" : sum.toString());

        this.calcDate = new StringElement(calcDate == null ? "-" : XmlLoader.formatDate(calcDate));

        this.sumTotal = sumTotal == null ? null : new StringElement(XmlLoader.formatSum(sumTotal));
    }
}
