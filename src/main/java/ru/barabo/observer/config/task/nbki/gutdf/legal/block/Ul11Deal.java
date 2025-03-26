package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 11. Общие сведения о сделке
 */
public class Ul11Deal {

    @XStreamAlias("role")
    private final StringElement role; //11.1. Код вида участия в сделке

    @XStreamAlias("code")
    private final StringElement code; //11.3. Код типа сделки

    @XStreamAlias("kindCode")
    private final StringElement kindCode; //11.4. Код вида займа (кредита) minOccurs="0"

    @XStreamAlias("purposeCode")
    private final StringElement purposeCode; //11.5. Код цели займа (кредита) minOccurs="0"

    @XStreamAlias("cardExist_0")
    private final StringElement cardExist0 = new StringElement(""); //18.7. Признак использования платежной карты = 0

    @XStreamAlias("novationExist_0")
    private final StringElement novationExist0 = new StringElement(""); //18.8. Признак возникновения обязательства в результате новации = 0

    @XStreamAlias("monetarySourceExist_1")
    private final StringElement monetarySourceExist_ = new StringElement(""); //18.10. Признак денежного обязательства субъекта = 1

    @XStreamAlias("monetarySubjectExist_1")
    private final StringElement monetarySubjectExist1 = new StringElement(""); //18.10. Признак денежного обязательства субъекта = 1

    @XStreamAlias("endDate")
    private final StringElement endDate; //18.11. Дата прекращения обязательства субъекта по условиям сделки emptyValueType xs:date

    @XStreamAlias("partialExist_0")
    private final StringElement partialExist0 = new StringElement(""); //18.13. Признак возникновения обязательства в результате получения части прав кредитора от другого лица = 0

    @XStreamAlias("creditLineExist_0")
    private final StringElement creditLineExist0; //18.14. Признак кредитной линии = 0

    @XStreamAlias("creditLineExist_1")
    private final StringElement creditLineExist1; //18.14. Признак кредитной линии = 1

    @XStreamAlias("creditLineCode")
    private final StringElement creditLineCode; //18.15. Код типа кредитной линии 1-Кредитная линия с лимитом выдачи

    @XStreamAlias("floatRateExist_0")
    private final StringElement floatRateExist0; //18.16. Признак плавающей (переменной) процентной ставки = 0

    @XStreamAlias("floatRateExist_1")
    private final StringElement floatRateExist1; //18.16. Признак плавающей (переменной) процентной ставки = 0

    @XStreamAlias("partialTransferExist_0")
    private final StringElement partialTransferExist0 = new StringElement(""); //18.17. Признак частичной передачи прав кредитора другому лицу = 0

    @XStreamAlias("startDate")
    private final StringElement startDate; //18.19. Дата возникновения обязательства субъекта

    @XStreamAlias("transferFact_0")
    private final StringElement transferFact0 = new StringElement(""); //18.21. Признак перевода (перехода) обязательства субъекта = 0

    @XStreamAlias("partnerFinancingFact_0")
    private final StringElement partnerFinancingFact0 = new StringElement(""); //18.22. Признак партнерского финансирования = 0

    public Ul11Deal(Integer role, Integer code, String kindCode, String purposeCode, Date endDate,
                    String creditLineCode, Date startDate, Boolean isFloatRate) {

        this.role = new StringElement(role.toString());
        this.code = new StringElement(code.toString());

        this.kindCode = (kindCode == null) || ((role != 1) && (role != 5) ) ? null : new StringElement(kindCode);

        this.purposeCode = (purposeCode == null) || ((role != 1) && (role != 5) ) ? null : new StringElement(purposeCode);

        if(isFloatRate) {
            this.floatRateExist0 = null;
            this.floatRateExist1 = new StringElement("");
        } else {
            this.floatRateExist0 = new StringElement("");
            this.floatRateExist1 = null;
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

    public Boolean isFloatRate() {
        return floatRateExist1 != null;
    }

    public StringElement getStartDate() {
        return startDate;
    }
}