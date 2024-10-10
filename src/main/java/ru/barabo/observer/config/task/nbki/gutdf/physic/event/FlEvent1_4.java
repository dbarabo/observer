package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*;

import java.util.Date;
import java.util.List;

public class FlEvent1_4 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "A";

    @XStreamAlias("FL_8_AddrReg")
    final Fl8AddrReg fl8AddrReg; // Блок 8. Регистрация физического лица по месту жительства или пребывания

    @XStreamAlias("FL_9_AddrFact")
    final Fl9AddrFact fl9AddrFact; // Блок 9. Фактическое место жительства

    @XStreamAlias("FL_10_Contact")
    final Fl10Contact fl10Contact; // Блок 10. Контактные данные

    @XStreamAlias("FL_11_IndividualEntrepreneur")
    final Fl11IndividualEntrepreneur fl11IndividualEntrepreneur; // Блок 11. Государственная регистрация в качестве индивидуального предпринимателя

    @XStreamAlias("FL_12_Capacity")
    final Fl12Capacity fl12Capacity; // Блок 12. Сведения о дееспособности

    @XStreamAlias("FL_17_DealUid")
    final Fl17DealUid fl17DealUid; // Блок 17. Идентификатор сделки

    @XStreamAlias("FL_18_Deal")
    final Fl18Deal fl18Deal; // Блок 18. Общие сведения о сделке

    @XStreamAlias("FL_19_Amount")
    final Fl19Amount fl19Amount; // Блок 19. Сумма и валюта обязательства

    @XStreamImplicit(itemFieldName = "FL_19_1_AmountInfo")
    final List<Fl19_1AmountInfo> fl19_1AmountInfoList; //  maxOccurs="unbounded" Блок 19(1). Сведения об обеспечиваемом обязательстве

    @XStreamAlias("FL_21_PaymentTerms")
    final Fl21PaymentTerms fl21PaymentTerms; // Блок 21. Сведения об условиях платежей

    @XStreamAlias("FL_20_JointDebtors")
    final Fl20JointDebtors fl20JointDebtors; // Блок 20. Сведения о солидарных должниках

    @XStreamAlias("FL_22_TotalCost")
    final Fl22TotalCost fl22TotalCost; // minOccurs="0" Блок 22. Полная стоимость потребительского кредита (займа)

    @XStreamAlias("FL_29_1_DebtBurdenInfo")
    final Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo; // minOccurs="0" Блок 29(1). Сведения о долговой нагрузке заемщика

    @XStreamAlias("FL_54_Accounting")
    final Fl54Accounting fl54Accounting; // Блок 54. Сведения об учете обязательства

    @XStreamAlias("FL_55_Application")
    final Fl55Application fl55Application; //minOccurs="0"

    @XStreamAlias("FL_56_Participation")
    final Fl56Participation fl56Participation; // Блок 56. Сведения об участии в обязательстве, по которому формируется кредитная история

    public FlEvent1_4(Integer orderNum, Date eventDate, Fl8AddrReg fl8AddrReg, Fl9AddrFact fl9AddrFact,
                      Fl10Contact fl10Contact, Fl11IndividualEntrepreneur fl11IndividualEntrepreneur,
                      Fl12Capacity fl12Capacity, Fl17DealUid fl17DealUid, Fl18Deal fl18Deal, Fl19Amount fl19Amount,
                      List<Fl19_1AmountInfo> fl19_1AmountInfoList, Fl21PaymentTerms fl21PaymentTerms,
                      Fl20JointDebtors fl20JointDebtors, Fl22TotalCost fl22TotalCost,
                      Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo, Fl54Accounting fl54Accounting,
                      Fl55Application fl55Application, Fl56Participation fl56Participation) {
        super(orderNum, eventDate);

        this.fl8AddrReg = fl8AddrReg;

        this.fl9AddrFact = fl9AddrFact;

        this.fl10Contact = fl10Contact;

        this.fl11IndividualEntrepreneur = fl11IndividualEntrepreneur;

        this.fl12Capacity = fl12Capacity;

        this.fl17DealUid = fl17DealUid;

        this.fl18Deal = fl18Deal;

        this.fl19Amount = fl19Amount;

        this.fl19_1AmountInfoList = fl19_1AmountInfoList;

        this.fl21PaymentTerms = fl21PaymentTerms;

        this.fl20JointDebtors = fl20JointDebtors;

        this.fl22TotalCost = fl22TotalCost;

        this.fl29_1DebtBurdenInfo = fl29_1DebtBurdenInfo;

        this.fl54Accounting = fl54Accounting;

        this.fl55Application = fl55Application;

        this.fl56Participation = fl56Participation;
    }

    @Override
    public String getEvent() {
        return "1.4";
    }

    @Override
    public String getUnicalId() {
        return fl17DealUid.getUid().value;
    }

    public Fl8AddrReg getFl8AddrReg() {
        return fl8AddrReg;
    }

    public Fl9AddrFact getFl9AddrFact() {
        return fl9AddrFact;
    }

    public Fl10Contact getFl10Contact() {
        return fl10Contact;
    }

    public Fl11IndividualEntrepreneur getFl11IndividualEntrepreneur() {
        return fl11IndividualEntrepreneur;
    }

    public Fl17DealUid getFl17DealUid() {
        return fl17DealUid;
    }

    public Fl18Deal getFl18Deal() {
        return fl18Deal;
    }

    public Fl19Amount getFl19Amount() {
        return fl19Amount;
    }

    public List<Fl19_1AmountInfo> getFl19_1AmountInfoList() {
        return fl19_1AmountInfoList;
    }

    public Fl21PaymentTerms getFl21PaymentTerms() {
        return fl21PaymentTerms;
    }

    public Fl20JointDebtors getFl20JointDebtors() {
        return fl20JointDebtors;
    }

    public Fl22TotalCost getFl22TotalCost() {
        return fl22TotalCost;
    }

    public Fl54Accounting getFl54Accounting() {
        return fl54Accounting;
    }

    public Fl55Application getFl55Application() {
        return fl55Application;
    }

    public Fl56Participation getFl56Participation() {
        return fl56Participation;
    }
}
