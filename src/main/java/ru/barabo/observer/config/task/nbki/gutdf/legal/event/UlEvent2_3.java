package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*;

import java.util.Date;
import java.util.List;

/**
 * Изменение задолженности, в том числе ввиду исполнения обязательства, наступила ответственность поручителя или обязательство принципала возместить выплаченную сумму
 */
public class UlEvent2_3 extends AbstractEventData {

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

    @XStreamAlias("UL_27_1_ProvisionPaymentOffset")
    final Ul271ProvisionPaymentOffset ul271ProvisionPaymentOffset; // Блок 27(1). Сведения о погашении требований кредитора по обязательству предоставлением отступного, зачетом встречных требований, прощением долга

    @XStreamAlias("UL_44_Accounting")
    final Ul44Accounting ul44Accounting; // Блок 44. Сведения об учете обязательства

    @XStreamAlias("UL_45_Application")
    final Ul45Application ul45Application; // Блок 45. Сведения об обращении субъекта к источнику с предложением совершить сделку

    @XStreamAlias("UL_46_Participation")
    final Ul46Participation ul46Participation; // Блок 46. Сведения об участии в обязательстве, по которому формируется кредитная история

    public UlEvent2_3(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, Ul11Deal ul11Deal,
                      Ul12Amount ul12Amount, List<Ul12_1AmountInfo> ul121AmountInfoList,
                      Ul14PaymentTerms ul14PaymentTerms, Ul17_18_19_20Group ul17181920Group,
                      Ul13JointDebtors ul13JointDebtors, Ul271ProvisionPaymentOffset ul271ProvisionPaymentOffset, Ul44Accounting ul44Accounting,
                      Ul45Application ul45Application, Ul46Participation ul46Participation) {

        super(orderNum, eventDate, "2.3");

        this.ul10DealUid = ul10DealUid;
        this.ul11Deal = ul11Deal;
        this.ul12Amount = ul12Amount;
        ul12_1AmountInfoList = ul121AmountInfoList;
        this.ul14PaymentTerms = ul14PaymentTerms;
        ul17_18_19_20Group = ul17181920Group;
        this.ul13JointDebtors = ul13JointDebtors;
        this.ul271ProvisionPaymentOffset = ul271ProvisionPaymentOffset;
        this.ul44Accounting = ul44Accounting;
        this.ul45Application = ul45Application;
        this.ul46Participation = ul46Participation;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "2.3";
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

    public Ul44Accounting getUl44Accounting() {
        return ul44Accounting;
    }

    public Ul45Application getUl45Application() {
        return ul45Application;
    }

    public Ul46Participation getUl46Participation() {
        return ul46Participation;
    }
}
