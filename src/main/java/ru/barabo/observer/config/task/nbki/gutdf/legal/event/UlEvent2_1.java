package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*;

import java.util.Date;
import java.util.List;

/**
 * Изменились сведения об условиях обязательства субъекта
 */
public class UlEvent2_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("UL_10_DealUid")
    final Ul10DealUid ul10DealUid; // Блок 10. Идентификатор сделки

    @XStreamAlias("UL_11_Deal")
    final Ul11Deal ul11Deal; // Блок 11. Общие сведения о сделке

    @XStreamAlias("UL_12_Amount")
    final Ul12Amount ul12Amount; // Блок 12. Сумма и валюта обязательства

    @XStreamImplicit(itemFieldName = "UL_12_1_AmountInfo")
    final List<Ul12_1AmountInfo> ul12_1AmountInfoList; //  maxOccurs="unbounded" Блок 19(1). Сведения об обеспечиваемом обязательстве

    @XStreamAlias("UL_14_PaymentTerms")
    final Ul14PaymentTerms ul14PaymentTerms; // Блок 14. Сведения об условиях платежей

    @XStreamAlias("UL_17_18_19_20_Group")
    final Ul17_18_19_20Group ul17_18_19_20Group; // Блок 17-20

    @XStreamAlias("UL_13_JointDebtors")
    final Ul13JointDebtors ul13JointDebtors; // Блок 13. Сведения о солидарных должниках

    @XStreamAlias("UL_15_ContractChanges")
    final Ul15ContractChanges ul15ContractChanges; // Блок 15. Сведения об изменении договора

    @XStreamAlias("UL_15_1_ContractTermsChanges")
    final Ul15_1ContractTermsChanges ul15_1ContractTermsChanges; //Блок 15(1). Сведения об изменении условий обязательства в результате наступления указанного в сделке события

    @XStreamAlias("UL_44_Accounting")
    final Ul44Accounting ul44Accounting; // Блок 44. Сведения об учете обязательства

    public UlEvent2_1(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, Ul11Deal ul11Deal,
                      Ul12Amount ul12Amount, List<Ul12_1AmountInfo> ul121AmountInfoList,
                      Ul14PaymentTerms ul14PaymentTerms, Ul17_18_19_20Group ul17181920Group,
                      Ul13JointDebtors ul13JointDebtors, Ul15ContractChanges ul15ContractChanges,
                      Ul15_1ContractTermsChanges ul151ContractTermsChanges, Ul44Accounting ul44Accounting) {

        super(orderNum, eventDate, "2.1");

        this.ul10DealUid = ul10DealUid;
        this.ul11Deal = ul11Deal;
        this.ul12Amount = ul12Amount;
        ul12_1AmountInfoList = ul121AmountInfoList;
        this.ul14PaymentTerms = ul14PaymentTerms;
        ul17_18_19_20Group = ul17181920Group;
        this.ul13JointDebtors = ul13JointDebtors;
        this.ul15ContractChanges = ul15ContractChanges;
        ul15_1ContractTermsChanges = ul151ContractTermsChanges;
        this.ul44Accounting = ul44Accounting;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "2.1";
    }

    @Override
    public String getUnicalId() {
        return ul10DealUid.getUid().value;
    }

    public Ul10DealUid getUl10DealUid() {
        return ul10DealUid;
    }

    public Ul11Deal getUl11Deal() {
        return ul11Deal;
    }

    public Ul12Amount getUl12Amount() {
        return ul12Amount;
    }

    public List<Ul12_1AmountInfo> getUl12_1AmountInfoList() {
        return ul12_1AmountInfoList;
    }

    public Ul14PaymentTerms getUl14PaymentTerms() {
        return ul14PaymentTerms;
    }

    public Ul17_18_19_20Group getUl17_18_19_20Group() {
        return ul17_18_19_20Group;
    }

    public Ul15ContractChanges getUl15ContractChanges() {
        return ul15ContractChanges;
    }

    public Ul15_1ContractTermsChanges getUl15_1ContractTermsChanges() {
        return ul15_1ContractTermsChanges;
    }

    public Ul44Accounting getUl44Accounting() {
        return ul44Accounting;
    }
}
