package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*;

import java.util.Date;
import java.util.List;

/**
 * Изменились сведения об условиях обязательства субъекта
 */
public class FlEvent2_1 extends AbstractEventData  {

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

    @XStreamAlias("FL_25_26_27_28_Group")
    final Fl25_26_27_28Group fl25_26_27_28Group; // Блок 25-28

    @XStreamAlias("FL_29_MonthlyPayment")
    final Fl29MonthlyPayment fl29MonthlyPayment; // minOccurs="0" Блок 29. Величина среднемесячного платежа по договору займа (кредита) и дата ее расчета

    @XStreamAlias("FL_29_1_DebtBurdenInfo")
    final Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo; // minOccurs="0" Блок 29(1). Сведения о долговой нагрузке заемщика

    @XStreamAlias("FL_20_JointDebtors")
    final Fl20JointDebtors fl20JointDebtors; // Блок 20. Сведения о солидарных должниках

    @XStreamAlias("FL_23_ContractChanges")
    final Fl23ContractChanges fl23ContractChanges; // Блок 23. Сведения об изменении договора

    @XStreamAlias("FL_23_1_ContractTermsChanges")
    final Fl23_1ContractTermsChanges fl23_1ContractTermsChanges; // Блок 23(1). Сведения об изменении условий обязательства в результате наступления указанного в сделке события

    @XStreamAlias("FL_54_Accounting")
    final Fl54Accounting fl54Accounting; // Блок 54. Сведения об учете обязательства


    public FlEvent2_1(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, Fl18Deal fl18Deal,
                      Fl19Amount fl19Amount, List<Fl19_1AmountInfo> fl191AmountInfoList,
                      Fl21PaymentTerms fl21PaymentTerms, Fl22TotalCost fl22TotalCost,
                      Fl25_26_27_28Group fl25262728Group, Fl29MonthlyPayment fl29MonthlyPayment,
                      Fl29_1DebtBurdenInfo fl291DebtBurdenInfo, Fl20JointDebtors fl20JointDebtors,
                      Fl23ContractChanges fl23ContractChanges, Fl23_1ContractTermsChanges fl231ContractTermsChanges,
                      Fl54Accounting fl54Accounting) {
        super(orderNum, eventDate);

        this.fl17DealUid = fl17DealUid;
        this.fl18Deal = fl18Deal;
        this.fl19Amount = fl19Amount;
        fl19_1AmountInfoList = fl191AmountInfoList;
        this.fl21PaymentTerms = fl21PaymentTerms;
        this.fl22TotalCost = fl22TotalCost;
        fl25_26_27_28Group = fl25262728Group;
        this.fl29MonthlyPayment = fl29MonthlyPayment;
        fl29_1DebtBurdenInfo = fl291DebtBurdenInfo;
        this.fl20JointDebtors = fl20JointDebtors;
        this.fl23ContractChanges = fl23ContractChanges;
        fl23_1ContractTermsChanges = fl231ContractTermsChanges;
        this.fl54Accounting = fl54Accounting;
    }
}