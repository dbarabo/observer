package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

public class Fl18Deal {

    @XStreamAlias("role")
    private final StringElement role; //18.1. Код вида участия в сделке

    @XStreamAlias("code")
    private final StringElement code; //18.3. Код типа сделки

    @XStreamAlias("kindCode")
    private final StringElement kindCode; //18.4. Код вида займа (кредита) minOccurs="0"

    @XStreamAlias("purposeCode")
    private final StringElement purposeCode; //18.5. Код цели займа (кредита) minOccurs="0"

    @XStreamAlias("consumerExist_0")
    private final StringElement consumerExist0; //18.6. Признак потребительского кредита (займа) = 0

    @XStreamAlias("consumerExist_1")
    private final StringElement consumerExist1; //18.6. Признак потребительского кредита (займа) = 1

    @XStreamAlias("cardExist_0")
    private final StringElement cardExist0 = new StringElement(""); //18.7. Признак использования платежной карты = 0

    @XStreamAlias("novationExist_0")
    private final StringElement novationExist0 = new StringElement(""); //18.8. Признак возникновения обязательства в результате новации = 0

    @XStreamAlias("monetarySubjectExist_1")
    private final StringElement monetarySubjectExist1 = new StringElement(""); //18.10. Признак денежного обязательства субъекта = 1

    @XStreamAlias("endDate")
    private final StringElement endDate; //18.11. Дата прекращения обязательства субъекта по условиям сделки emptyValueType xs:date

    @XStreamAlias("creditorCode")
    private final StringElement creditorCode = new StringElement("1"); //18.12. Код вида кредитора – заимодавца 1-кредитная организация

    @XStreamAlias("partialExist_0")
    private final StringElement partialExist0 = new StringElement(""); //18.13. Признак возникновения обязательства в результате получения части прав кредитора от другого лица = 0

    @XStreamAlias("creditLineExist_0")
    private final StringElement creditLineExist0; //18.14. Признак кредитной линии = 0

    @XStreamAlias("creditLineExist_1")
    private final StringElement creditLineExist1; //18.14. Признак кредитной линии = 1

    @XStreamAlias("creditLineCode")
    private final StringElement creditLineCode; //18.15. Код типа кредитной линии 1-Кредитная линия с лимитом выдачи

    @XStreamAlias("floatRateExist_0")
    private final StringElement floatRateExist0 = new StringElement(""); //18.16. Признак плавающей (переменной) процентной ставки = 0

    @XStreamAlias("partialTransferExist_0")
    private final StringElement partialTransferExist0 = new StringElement(""); //18.17. Признак частичной передачи прав кредитора другому лицу = 0

    @XStreamAlias("startDate")
    private final StringElement startDate; //18.19. Дата возникновения обязательства субъекта

    @XStreamAlias("repaymentFact_0")
    private final StringElement repaymentFact0; //18.20. Признак погашения по графику платежей = 0

    @XStreamAlias("repaymentFact_1")
    private final StringElement repaymentFact1; //18.20. Признак погашения по графику платежей = 1

    @XStreamAlias("transferFact_0")
    private final StringElement transferFact0 = new StringElement(""); //18.21. Признак перевода (перехода) обязательства субъекта = 0

    @XStreamAlias("partnerFinancingFact_0")
    private final StringElement partnerFinancingFact0 = new StringElement(""); //18.22. Признак партнерского финансирования = 0

    public Fl18Deal(Integer role, Integer code, String kindCode, String purposeCode, Boolean isConsumer,
                    Date endDate, String creditLineCode, Date startDate, Boolean isRepaymentFact) {

        this.role = new StringElement(role.toString());
        this.code = new StringElement(code.toString());

        this.kindCode = kindCode == null ? null : new StringElement(kindCode);

        this.purposeCode = purposeCode == null ? null : new StringElement(purposeCode);

        if(isConsumer) {
            this.consumerExist0 = null;
            this.consumerExist1 = new StringElement("");
        } else {
            this.consumerExist0 = new StringElement("");
            this.consumerExist1 = null;
        }

        this.endDate = new StringElement(endDate == null ? "-" : XmlLoader.formatDate(endDate));

        if(creditLineCode != null && (!(creditLineCode.isEmpty()) ) ) {
            this.creditLineExist0 = null;
            this.creditLineExist1 = new StringElement("");

            this.creditLineCode = new StringElement(creditLineCode);

        } else {
            this.creditLineExist0 = new StringElement("");
            this.creditLineExist1 = null;

            this.creditLineCode = null;
        }

        this.startDate = new StringElement(XmlLoader.formatDate(startDate));

        if(isRepaymentFact) {
            this.repaymentFact0 = null;
            this.repaymentFact1 = new StringElement("");
        } else {
            this.repaymentFact0 = new StringElement("");
            this.repaymentFact1 = null;
        }
    }

    public Boolean isConsumer() {
        return consumerExist1 != null;
    }

    public StringElement getRole() {
        return role;
    }

    public StringElement getCode() {
        return code;
    }

    public StringElement getKindCode() {
        return kindCode;
    }

    public StringElement getPurposeCode() {
        return purposeCode;
    }

    public StringElement getEndDate() {
        return endDate;
    }

    public StringElement getCreditLineCode() {
        return creditLineCode;
    }

    public StringElement getStartDate() {
        return startDate;
    }
}
