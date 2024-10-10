package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 21. Сведения об условиях платежей
 */
public class Fl21PaymentTerms {

    @XStreamAlias("mainPaySum")
    private final StringElement mainPaySum; //21.1. Сумма ближайшего следующего платежа по основному долгу 0.00 emptyValueType dec15p2Type

    @XStreamAlias("mainPayDate")
    private final StringElement mainPayDate; //minOccurs="0" 21.2. Дата ближайшего следующего платежа по основному долгу

    @XStreamAlias("percentPaySum")
    private final StringElement percentPaySum; //21.3. Сумма ближайшего следующего платежа по процентам 0.00 emptyValueType dec15p2Type

    @XStreamAlias("percentPayDate")
    private final StringElement percentPayDate; //minOccurs="0" 21.4. Дата ближайшего следующего платежа по процентам

    @XStreamAlias("freqCode")
    private final StringElement freqCode; //minOccurs="0" 21.5. Код частоты платежей

    @XStreamAlias("minCardPay")
    private final StringElement minCardPay; //minOccurs="0" 21.6. Сумма минимального платежа по кредитной карте

    @XStreamAlias("graceDate")
    private final StringElement graceDate; //minOccurs="0" 21.7. Дата начала беспроцентного периода

    @XStreamAlias("graceEndDate")
    private final StringElement graceEndDate; //minOccurs="0" 21.8. Дата окончания беспроцентного периода

    @XStreamAlias("percentEndDate")
    private final StringElement percentEndDate; //minOccurs="0" 21.9. Дата окончания срока уплаты процентов

    public Fl21PaymentTerms() {
        this.mainPaySum = new StringElement("0.00");
        this.mainPayDate = null;
        this.percentPaySum = new StringElement("0.00");
        this.percentPayDate = null;
        this.freqCode = null;
        this.minCardPay = null;
        this.graceDate = null;
        this.graceEndDate = null;
        this.percentEndDate = null;
    }

    public Fl21PaymentTerms(Number mainPaySum, Date mainPayDate, Number percentPaySum, Date percentPayDate,
                            Integer freqCode, Date percentEndDate) {

        this.mainPaySum = new StringElement(XmlLoader.formatSum(mainPaySum));
        this.mainPayDate = mainPayDate == null ? null : new StringElement(XmlLoader.formatDate(mainPayDate));
        this.percentPaySum = new StringElement(XmlLoader.formatSum(percentPaySum));
        this.percentPayDate = percentPayDate == null ? null :  new StringElement(XmlLoader.formatDate(percentPayDate));
        this.freqCode = freqCode == null ? null : new StringElement(freqCode.toString());
        this.minCardPay = null;
        this.graceDate = null;
        this.graceEndDate = null;
        this.percentEndDate = percentEndDate == null ? null : new StringElement(XmlLoader.formatDate(percentEndDate));;
    }

    public StringElement getMainPaySum() {
        return mainPaySum;
    }

    public StringElement getMainPayDate() {
        return mainPayDate;
    }

    public StringElement getPercentPaySum() {
        return percentPaySum;
    }

    public StringElement getPercentPayDate() {
        return percentPayDate;
    }

    public StringElement getFreqCode() {
        return freqCode;
    }

    public StringElement getPercentEndDate() {
        return percentEndDate;
    }
}
