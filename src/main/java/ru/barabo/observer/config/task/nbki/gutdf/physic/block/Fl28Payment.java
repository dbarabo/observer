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

        this.paymentMainSum = new StringElement(XmlLoader.formatSum(0));
        this.paymentPercentSum = new StringElement(XmlLoader.formatSum(0));
        this.paymentOtherSum = new StringElement(XmlLoader.formatSum(0));
        this.totalSum = new StringElement(XmlLoader.formatSum(0));
        this.totalMainSum = new StringElement(XmlLoader.formatSum(0));
        this.totalPercentSum = new StringElement(XmlLoader.formatSum(0));
        this.totalOtherSum = new StringElement(XmlLoader.formatSum(0));
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

        this.paymentMainSum = new StringElement(XmlLoader.formatSum(paymentMainSum == null ? 0 : paymentMainSum));

        this.paymentPercentSum = new StringElement(XmlLoader.formatSum(paymentPercentSum == null ? 0 : paymentPercentSum));

        this.paymentOtherSum = new StringElement(XmlLoader.formatSum(paymentOtherSum == null ? 0 : paymentOtherSum));

        this.totalSum = new StringElement(XmlLoader.formatSum(totalSum == null ? 0 : totalSum));

        this.totalMainSum = new StringElement(XmlLoader.formatSum(totalMainSum == null ? 0 : totalMainSum));

        this.totalPercentSum = new StringElement(XmlLoader.formatSum(totalPercentSum == null ? 0 : totalPercentSum));

        this.totalOtherSum = new StringElement(XmlLoader.formatSum(totalOtherSum == null ? 0 : totalOtherSum));

        this.date = date == null ? null : new StringElement(XmlLoader.formatDate(date));

        this.sizeCode = new StringElement(sizeCode.toString());

        this.scheduleCode = new StringElement(scheduleCode.toString());

        this.lastMissPaySum = lastMissPaySum == null ? null : new StringElement(XmlLoader.formatSum(lastMissPaySum));

        this.paySum24 = paySum24 == null ? null : new StringElement(XmlLoader.formatSum(paySum24));
    }

    public StringElement getPaymentSum() {
        return paymentSum;
    }

    public StringElement getPaymentMainSum() {
        return paymentMainSum;
    }

    public StringElement getPaymentPercentSum() {
        return paymentPercentSum;
    }

    public StringElement getPaymentOtherSum() {
        return paymentOtherSum;
    }

    public StringElement getTotalSum() {
        return totalSum;
    }

    public StringElement getTotalMainSum() {
        return totalMainSum;
    }

    public StringElement getTotalPercentSum() {
        return totalPercentSum;
    }

    public StringElement getTotalOtherSum() {
        return totalOtherSum;
    }

    public StringElement getDate() {
        return date;
    }

    public StringElement getSizeCode() {
        return sizeCode;
    }

    public StringElement getScheduleCode() {
        return scheduleCode;
    }

    public StringElement getLastMissPaySum() {
        return lastMissPaySum;
    }

    public StringElement getPaySum24() {
        return paySum24;
    }
}
