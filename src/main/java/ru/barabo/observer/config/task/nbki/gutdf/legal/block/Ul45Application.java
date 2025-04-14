package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 45. Сведения об обращении субъекта к источнику с предложением совершить сделку
 */
public class Ul45Application {

    @XStreamAlias("role")
    private final StringElement role; // 55.1. Код вида участия в сделке

    @XStreamAlias("sum")
    private final StringElement sum; // 55.2. Сумма запрошенного займа (кредита), лизинга или обеспечения  Dec15p2  minOccurs="0"

    @XStreamAlias("currency")
    private final StringElement currency; // 55.3. Запрошенная валюта обязательства  minOccurs="0"

    @XStreamAlias("uid")
    private final StringElement uid; // 55.4. УИд обращения

    @XStreamAlias("applicationDate")
    private final StringElement applicationDate; // 55.5. Дата обращения

    @XStreamAlias("sourceCode")
    private final StringElement sourceCode = new StringElement("1"); //55.6. Код источника

    @XStreamAlias("wayCode")
    private final StringElement wayCode = new StringElement("6"); //55.7. Код способа обращения

    @XStreamAlias("approvalEndDate")
    private final StringElement approvalEndDate; //55.8. minOccurs="0" Дата окончания действия инвестиционного предложения, одобрения обращения (оферты кредитора)

    @XStreamAlias("stageEndDate")
    private final StringElement stageEndDate; //55.9. minOccurs="0" Дата окончания срока рассмотрения обращения

    @XStreamAlias("purposeCode")
    private final StringElement purposeCode; //55.10. minOccurs="0" Код цели запрошенного займа (кредита)

    @XStreamAlias("stageCode")
    private StringElement stageCode; //55.11. minOccurs="0" Код стадии рассмотрения обращения

    @XStreamAlias("stageDate")
    private final StringElement stageDate; //55.12. Дата перехода обращения в текущую стадию рассмотрения

    @XStreamAlias("applicationCode")
    private final StringElement code = new StringElement("6"); //emptyValueType 55.13. Код вида обращения

    @XStreamAlias("num")
    private final StringElement num; //55.14. Номер обращения minOccurs="0"

    @XStreamAlias("loanSum")
    private final StringElement loanSum; //55.15. Сумма одобренного займа (кредита), лизинга или обеспечения Dec15p2 minOccurs="0"

    public Ul45Application(String role, Number sum, String uid, Date applicationDate, Date approvalEndDate, Date stageEndDate,
                           String purposeCode, String stageCode, Date stageDate, String num, Number loanSum) {

        this.role = new StringElement(role);

        this.sum = sum == null ? null : new StringElement(XmlLoader.formatSum(sum) );

        currency = sum == null ? null : new StringElement("RUB");

        this.uid = new StringElement(uid);

        this.applicationDate = new StringElement(XmlLoader.formatDate(applicationDate) );

        this.approvalEndDate = approvalEndDate == null ? null : new StringElement(XmlLoader.formatDate(approvalEndDate) );

        this.stageEndDate = stageEndDate == null ? null : new StringElement(XmlLoader.formatDate(stageEndDate) );

        this.purposeCode = (purposeCode == null) || ((!role.equals("1")) && (!role.equals("5"))) ? null : new StringElement(purposeCode);

        this.stageCode = stageCode == null ? null : new StringElement(stageCode);

        this.stageDate = new StringElement(XmlLoader.formatDate(stageDate) );

        this.num = num == null ? null : new StringElement(num);

        this.loanSum = loanSum == null ? null : new StringElement(XmlLoader.formatSum(loanSum));
    }

    public StringElement getRole() {
        return role;
    }

    public StringElement getSum() {
        return sum;
    }

    public StringElement getUid() {
        return uid;
    }

    public StringElement getApplicationDate() {
        return applicationDate;
    }

    public StringElement getApprovalEndDate() {
        return approvalEndDate;
    }

    public StringElement getStageEndDate() {
        return stageEndDate;
    }

    public StringElement getPurposeCode() {
        return purposeCode;
    }

    public StringElement getStageCode() {
        return stageCode;
    }

    public void setStageCode(StringElement stageCode) {
        this.stageCode = stageCode;
    }

    public StringElement getStageDate() {
        return stageDate;
    }

    public StringElement getNum() {
        return num;
    }

    public StringElement getLoanSum() {
        return loanSum;
    }
}

