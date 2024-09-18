package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*;

import java.util.Date;
import java.util.List;

/**
 * Субъекту передана сумма займа (кредита), предмет лизинга передан лизингополучателю
 */
public class FlEvent2_2 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

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

    @XStreamAlias("FL_22_TotalCost")
    final Fl22TotalCost fl22TotalCost; // minOccurs="0" Блок 22. Полная стоимость потребительского кредита (займа)

    @XStreamAlias("FL_24_Fund")
    final Fl24Fund fl24Fund; // Блок 24. Дата передачи финансирования субъекту или возникновения обеспечения исполнения обязательства

    @XStreamAlias("FL_25_26_27_28_Group")
    final Fl25_26_27_28Group fl25_26_27_28Group; // Блок 25-28

    @XStreamAlias("FL_29_MonthlyPayment")
    final Fl29MonthlyPayment fl29MonthlyPayment; // minOccurs="0" Блок 29. Величина среднемесячного платежа по договору займа (кредита) и дата ее расчета

    @XStreamAlias("FL_29_1_DebtBurdenInfo")
    final Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo; // minOccurs="0" Блок 29(1). Сведения о долговой нагрузке заемщика

    @XStreamAlias("FL_20_JointDebtors")
    final Fl20JointDebtors fl20JointDebtors; // Блок 20. Сведения о солидарных должниках

    @XStreamAlias("FL_54_Accounting")
    final Fl54Accounting fl54Accounting; // Блок 54. Сведения об учете обязательства

    @XStreamAlias("FL_55_Application")
    final Fl55Application fl55Application; // minOccurs="0" Блок 55. Сведения об обращении субъекта к источнику с предложением совершить сделку

    @XStreamAlias("FL_56_Participation")
    final Fl56Participation fl56Participation; // Блок 56. Сведения об участии в обязательстве, по которому формируется кредитная история

    public FlEvent2_2(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, Fl18Deal fl18Deal, Fl19Amount fl19Amount,
                      List<Fl19_1AmountInfo> fl191AmountInfoList, Fl21PaymentTerms fl21PaymentTerms,
                      Fl22TotalCost fl22TotalCost, Fl24Fund fl24Fund, Fl25_26_27_28Group fl25262728Group,
                      Fl29MonthlyPayment fl29MonthlyPayment, Fl29_1DebtBurdenInfo fl291DebtBurdenInfo,
                      Fl20JointDebtors fl20JointDebtors, Fl54Accounting fl54Accounting,
                      Fl55Application fl55Application, Fl56Participation fl56Participation) {

        super(orderNum, eventDate);

        this.fl17DealUid = fl17DealUid;
        this.fl18Deal = fl18Deal;
        this.fl19Amount = fl19Amount;
        fl19_1AmountInfoList = fl191AmountInfoList;
        this.fl21PaymentTerms = fl21PaymentTerms;
        this.fl22TotalCost = fl22TotalCost;
        this.fl24Fund = fl24Fund;
        fl25_26_27_28Group = fl25262728Group;
        this.fl29MonthlyPayment = fl29MonthlyPayment;
        fl29_1DebtBurdenInfo = fl291DebtBurdenInfo;
        this.fl20JointDebtors = fl20JointDebtors;
        this.fl54Accounting = fl54Accounting;
        this.fl55Application = fl55Application;
        this.fl56Participation = fl56Participation;
    }
}
