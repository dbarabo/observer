package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 28. Сведения о внесении платежей
 */
public class Fl28Payment {

    @XStreamAlias("paymentSum")
    private final StringElement paymentSum; // dec15p2Type 28.2. Сумма последнего внесенного платежа

    @XStreamAlias("paymentMainSum")
    private final StringElement paymentMainSum; // dec15p2Type minOccurs="0" 28.3. Сумма последнего внесенного платежа по основному долгу

    @XStreamAlias("paymentPercentSum")
    private final StringElement paymentPercentSum; // dec15p2Type minOccurs="0" 28.4. Сумма последнего внесенного платежа по процентам

    @XStreamAlias("paymentOtherSum")
    private final StringElement paymentOtherSum; // dec15p2Type minOccurs="0" 28.5. Сумма последнего внесенного платежа по иным требованиям

    @XStreamAlias("totalSum")
    private final StringElement totalSum; // dec15p2Type minOccurs="0" 28.6. Сумма всех внесенных платежей по обязательству

    @XStreamAlias("totalMainSum")
    private final StringElement totalMainSum; // dec15p2Type minOccurs="0" 28.7. Сумма внесенных платежей по основному долгу

    @XStreamAlias("totalPercentSum")
    private final StringElement totalPercentSum; // dec15p2Type minOccurs="0" 28.8. Сумма внесенных платежей по процентам

    @XStreamAlias("totalOtherSum")
    private final StringElement totalOtherSum; // dec15p2Type minOccurs="0" 28.9. Сумма внесенных платежей по иным требованиям

    @XStreamAlias("date")
    private final StringElement date; // minOccurs="0" 28.1. Дата последнего внесенного платежа

    @XStreamAlias("sizeCode")
    private final StringElement sizeCode; // emptyValueType|refPaymentComplianceType 28.10. Код соблюдения размера платежей

    @XStreamAlias("scheduleCode")
    private final StringElement scheduleCode; // emptyValueType|refPaymentDeadlineType 28.11. Код соблюдения срока внесения платежей

    @XStreamAlias("lastMissPaySum")
    private final StringElement lastMissPaySum; // dec15p2Type minOccurs="0" 28.14. Сумма последнего внесенного платежа (части платежа) с просрочкой более 30 календарных дней

    @XStreamAlias("paySum24")
    private final StringElement paySum24; // dec15p2Type minOccurs="0" 28.15. Сумма внесенных платежей за 24 календарных месяца, за исключением платежей с просрочкой более 30 календарных дней

    public Fl28Payment(Integer sizeCode, Integer scheduleCode) {

        this.paymentSum = new StringElement(XmlLoader.formatSum(0));

        this.paymentMainSum = null;
        this.paymentPercentSum = null;
        this.paymentOtherSum = null;
        this.totalSum = null;
        this.totalMainSum = null;
        this.totalPercentSum = null;
        this.totalOtherSum = null;
        this.date = null;

        this.sizeCode = new StringElement(sizeCode.toString());

        this.scheduleCode = new StringElement(scheduleCode.toString());

        this.lastMissPaySum = null;
        this.paySum24 = null;
    }


    public Fl28Payment(Number paymentSum, Number paymentMainSum, Number paymentPercentSum, Number paymentOtherSum,
                       Number totalSum, Number totalMainSum, Number totalPercentSum, Number totalOtherSum,
                       Date date, Integer sizeCode, Integer scheduleCode, Number lastMissPaySum, Number paySum24) {

        this.paymentSum = new StringElement(XmlLoader.formatSum(paymentSum));

        this.paymentMainSum = paymentMainSum == null ? null : new StringElement(XmlLoader.formatSum(paymentMainSum));

        this.paymentPercentSum = paymentPercentSum == null ? null : new StringElement(XmlLoader.formatSum(paymentPercentSum));

        this.paymentOtherSum = paymentOtherSum == null ? null : new StringElement(XmlLoader.formatSum(paymentOtherSum));

        this.totalSum = totalSum == null ? null : new StringElement(XmlLoader.formatSum(totalSum));

        this.totalMainSum = totalMainSum == null ? null : new StringElement(XmlLoader.formatSum(totalMainSum));

        this.totalPercentSum = totalPercentSum == null ? null : new StringElement(XmlLoader.formatSum(totalPercentSum));

        this.totalOtherSum = totalOtherSum == null ? null : new StringElement(XmlLoader.formatSum(totalOtherSum));

        this.date = date == null ? null : new StringElement(XmlLoader.formatDate(date));

        this.sizeCode = new StringElement(sizeCode.toString());

        this.scheduleCode = new StringElement(scheduleCode.toString());

        this.lastMissPaySum = lastMissPaySum == null ? null : new StringElement(XmlLoader.formatSum(lastMissPaySum));

        this.paySum24 = paySum24 == null ? null : new StringElement(XmlLoader.formatSum(paySum24));
    }
}
